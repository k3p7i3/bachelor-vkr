import { createSlice, createAsyncThunk  } from "@reduxjs/toolkit";
import { middleware } from "../middleware";
import { mapToOrderServiceCalcCostRequest, mapToOrderCalcCostRequest, inputMapOrder, outputMapOrder } from "./orderMappers";
import { getOrder, postOrder, updateOrder, getClientOrders, getAgentOrders, executePayment } from "../../api/orderApi";
import { calculateOrderServiceCost, calculateOrderCost } from "../../api/tariffApi";

export const fetchOrder = createAsyncThunk('order/fetchOrder', async (params, thunk) => {
  return await middleware(getOrder, params, thunk, { outputMapper: outputMapOrder });
});

export const fetchClientOrders = createAsyncThunk('order/fetchClientOrders', async (params, thunk) => {
  return await middleware(getClientOrders, params, thunk, { });
});

export const fetchAgentOrders = createAsyncThunk('order/fetchAgentOrders', async (params, thunk) => {
  return await middleware(getAgentOrders, params, thunk, { });
});

export const calcOrderCost = createAsyncThunk('order/calcOrderCost', async (params, thunk) => {
  return await middleware(calculateOrderCost, params, thunk, {inputMapper: mapToOrderCalcCostRequest });
});

export const calcOrderServiceCost = createAsyncThunk('order/calcOrderServiceCost', async (params, thunk) => {
  return await middleware(calculateOrderServiceCost, params, thunk, {inputMapper: mapToOrderServiceCalcCostRequest });
});

export const createOrder = createAsyncThunk('order/createOrder', async (params, thunk) => {
  return await middleware(postOrder, params, thunk, {inputMapper: inputMapOrder, outputMapper: outputMapOrder });
});

export const saveOrder = createAsyncThunk('order/updateOrder', async (params, thunk) => {
  return await middleware(updateOrder, params, thunk, {inputMapper: inputMapOrder, outputMapper: outputMapOrder });
});

export const processPayment = createAsyncThunk('order/executePayment', async (params, thunk) => {
  return await middleware(executePayment, params, thunk, {});
})

export const getActiveValidTariffs = (appliedTariffs) => {
  return Object.values(appliedTariffs).filter((t) => !t.isDisabled && isAppliedTariffValid(t))
}

const isAppliedTariffValid = (appliedTariff) => {
  if (!appliedTariff.isDisabled) {
    const featuresValid = Object.keys(appliedTariff.selectedOptions).length === appliedTariff.featuresCount
    const includedProductsValid = appliedTariff.applyLevel === "ORDER" || appliedTariff.selectedProducts.length > 0
    return featuresValid && includedProductsValid
  }
  return true;
}

const getFeatures = (taxable) => {
  return {
    weight: taxable.weight,
    volume: taxable.volume,
    density: taxable.density,
    price: taxable.price,
    totalNumber: taxable.totalNumber
  }
}

const orderSlice = createSlice({
  name: 'order',
  initialState: {
    currentOrder: undefined,
    orders: undefined,
    paymentConfirmationToken: undefined,
    status: {
      message: undefined,
      code: undefined,
    },
    progress: false,
  },
  reducers: {
    resetStatus: (state, action) => {
      state.status = {
        message: undefined,
        code: undefined,
      }
    },
    resetPaymentToken: (state) => {
      state.paymentConfirmationToken = undefined;
    },
    initCreateOrder: (state, action) => {
      const cartProducts = action.payload.products;
      const orderProducts = cartProducts?.reduce((map, cartProduct) => {
        map[cartProduct.product.id] = { 
          productId: cartProduct.product.id,
          totalNumber: cartProduct.count,
          weight: cartProduct.product.weight && {...cartProduct.product.weight},
          boxVolume: cartProduct.product.boxVolume && {...cartProduct.product.boxVolume},
          volume: cartProduct.product.volume && {...cartProduct.product.volume},
          price: cartProduct.product.price && {...cartProduct.product.price}
        };
        return map;
      }, {});

      state.currentOrder = {
        id: undefined,
        userId: action.payload.userId,
        agentId: action.payload.agentId,
        orderProducts: orderProducts,
        appliedTariffs: undefined,
        price: undefined,
        totalCost: undefined
      };
    },
    initAppliedTariffs: (state, action) => {
      state.currentOrder.appliedTariffs = action.payload.tariffs.reduce((map, tariff) => {
        map[tariff.tariffId] = {
          tariffId: tariff.tariffId,
          isDisabled: true,
          applyLevel: tariff.applyLevel,
          featuresCount: tariff.features.length,
          selectedOptions: {},
          selectedProducts: [],
          cost: undefined
        }
        return map;
      }, {});
    },
    setOrderProductFeature: (state, action) => {
      const product = state.currentOrder.orderProducts[action.payload.productId]
      product[action.payload.type] = action.payload[action.payload.type]
    },
    setOrderFeature: (state, action) => {
      const order = state.currentOrder;
      order[action.payload.type] = action.payload[action.payload.type]
    },
    setAppliedTariff: (state, action) => {
      const appliedTariff = action.payload
      state.currentOrder.appliedTariffs[action.payload.tariffId] = appliedTariff

      if (!appliedTariff.isDisabled && !isAppliedTariffValid(appliedTariff)) {
        state.currentOrder.appliedTariffs[appliedTariff.tariffId].cost = undefined
      }
    }
  },

  extraReducers(builder) {
    builder
      .addCase(calcOrderServiceCost.fulfilled, (state, action) => {
        console.log(action.payload)

        const calculatedOrder = action.payload.order;
        const tariffId = action.payload.order.appliedTariffs?.[0]?.tariffId;
        if (tariffId) {
          const cost = action.payload.cost
          state.currentOrder.appliedTariffs[tariffId].cost = cost

          calculatedOrder.products.forEach((product) => {
            state.currentOrder.orderProducts[product.productId] = {
              ...state.currentOrder.orderProducts[product.productId],
              ...getFeatures(product)
            }
          })
          if (calculatedOrder.products.length === Object.keys(state.currentOrder.orderProducts)) {
            state.currentOrder = {
              ...state.currentOrder,
              ...getFeatures(calculatedOrder)
            }
          }
          state.currentOrder.totalCost = undefined
        }
        state.progress = false;
      })
      .addCase(calcOrderServiceCost.pending, (state, action) => {
        state.progress = true
      })
      .addCase(calcOrderServiceCost.rejected, (state, action) => {
        console.log(action)
        state.status = {
          message: `Не удалось рассчитать стоимость`,
          code: action.payload.code,
        }
        state.progress = false;
      })

      .addCase(calcOrderCost.fulfilled, (state, action) => {
        console.log(action.payload)

        const results = action.payload.calculatedTariffs
        const tariffIds = []
        results.forEach((result) => {
          const tariffId = result.order.appliedTariffs?.[0]?.tariffId;
          if (tariffId) {
            tariffIds.push(tariffId)
            const cost = result.cost
            state.currentOrder.appliedTariffs[tariffId].cost = cost 
          }
        })

        const calculatedOrder = action.payload.complexOrder;
        calculatedOrder.products.forEach((product) => {
          state.currentOrder.orderProducts[product.productId] = {
            ...state.currentOrder.orderProducts[product.productId],
            ...getFeatures(product)
          }
        })

        if (calculatedOrder.products.length === Object.keys(state.currentOrder.orderProducts).length) {
          state.currentOrder = {
            ...state.currentOrder,
            ...getFeatures(calculatedOrder)
          }
        }

        state.currentOrder.totalCost = action.payload.totalCost;
        state.progress = false;
      })
      .addCase(calcOrderCost.pending, (state, action) => {
        state.progress = true
      })
      .addCase(calcOrderCost.rejected, (state, action) => {
        console.log(action)
        state.status = {
          message: `Не удалось рассчитать стоимость `,
          code: action.payload.code,
        }
        state.progress = false;
      })

      .addCase(createOrder.fulfilled, (state, action) => {
        state.status = {
          message: "Заказ создан",
          code: 3,
        }
        state.currentOrder = action.payload
        state.progress = false;
      })
      .addCase(createOrder.pending, (state, action) => {
        state.progress = true
      })
      .addCase(createOrder.rejected, (state, action) => {
        state.status = {
          message: `Не удалось создать заказ ${action.payload.message}`,
          code: action.payload.code,
        }
        state.progress = false;
      })

      .addCase(fetchOrder.fulfilled, (state, action) => {
        state.currentOrder = action.payload
        state.progress = false;
      })
      .addCase(fetchOrder.pending, (state, action) => {
        state.progress = true
      })
      .addCase(fetchOrder.rejected, (state, action) => {
        state.status = {
          message: `Не удалось получить заказ ${action.payload.message}`,
          code: action.payload.code,
        }
        state.progress = false;
      })

      .addCase(fetchClientOrders.fulfilled, (state, action) => {
        state.orders = action.payload
        state.progress = false;
      })
      .addCase(fetchClientOrders.pending, (state, action) => {
        state.progress = true
      })
      .addCase(fetchClientOrders.rejected, (state, action) => {
        state.status = {
          message: `Не удалось получить заказы ${action.payload.message}`,
          code: action.payload.code,
        }
        state.progress = false;
      })

      .addCase(fetchAgentOrders.fulfilled, (state, action) => {
        state.orders = action.payload
        state.progress = false;
      })
      .addCase(fetchAgentOrders.pending, (state, action) => {
        state.progress = true
      })
      .addCase(fetchAgentOrders.rejected, (state, action) => {
        state.status = {
          message: `Не удалось получить заказы ${action.payload.message}`,
          code: action.payload.code,
        }
        state.progress = false;
      })

      .addCase(processPayment.fulfilled, (state, action) => {
        console.log(action.payload)
        state.paymentConfirmationToken = action.payload.confirmationToken
        state.progress = false;
      })
      .addCase(processPayment.pending, (state, action) => {
        state.progress = true
      })
      .addCase(processPayment.rejected, (state, action) => {
        console.log(action.payload)
        state.status = {
          message: `Не удалось провести оплату`,
          code: action.payload.code,
        }
        state.progress = false;
      })
  }
});

export const { 
  resetStatus, resetPaymentToken, initCreateOrder, setOrderProductFeature, 
  setOrderFeature, initAppliedTariffs, setAppliedTariff 
} = orderSlice.actions;

export const selectCurrentOrder = (state) => state.order.currentOrder;
export const selectConfirmationToken = (state) => state.order.paymentConfirmationToken;
export const selectOrders = (state) => state.order.orders;

export default orderSlice.reducer;