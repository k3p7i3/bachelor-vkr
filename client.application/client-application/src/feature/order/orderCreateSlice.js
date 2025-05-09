import { createSlice, createAsyncThunk  } from "@reduxjs/toolkit"
import { middleware } from "../middleware";
import { mapToOrderServiceCalcCostRequest, mapToOrderCalcCostRequest, mapToOrderCreateRequest } from "./createOrderFormMappers"
import { calculateOrderServiceCost, calculateOrderCost } from "../../api/tariffApi"
import { postOrder } from "../../api/orderApi";

export const calcOrderCostForCreateForm = createAsyncThunk('orderCreate/calcOrderCost', async (params, thunk) => {
  return await middleware(calculateOrderCost, params, thunk, {inputMapper: mapToOrderCalcCostRequest });
});

export const calcOrderServiceCostForCreateForm = createAsyncThunk('orderCreate/calcOrderServiceCost', async (params, thunk) => {
  return await middleware(calculateOrderServiceCost, params, thunk, {inputMapper: mapToOrderServiceCalcCostRequest });
});

export const createOrder = createAsyncThunk('orderCreate/createOrder', async (params, thunk) => {
  return await middleware(postOrder, params, thunk, {inputMapper: mapToOrderCreateRequest });
});

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
    boxVolume: taxable.boxVolume,
    density: taxable.density,
    price: taxable.price,
    totalNumber: taxable.totalNumber
  }
}

const orderCreateSlice = createSlice({
  name: 'orderCreate',
  initialState: {
    createOrder: undefined,
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

      state.createOrder = {
        userId: action.payload.userId,
        agentId: action.payload.agentId,
        orderProducts: orderProducts,
        appliedTariffs: undefined,
        price: undefined,
        totalCost: undefined
      };
    },
    initAppliedTariffs: (state, action) => {
      state.createOrder.appliedTariffs = action.payload.tariffs.reduce((map, tariff) => {
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

      state.createOrder.calcCostForTariffTasks = []
    },
    setOrderProductFeature: (state, action) => {
      const product = state.createOrder.orderProducts[action.payload.productId]

      if (product[action.payload.type] === action.payload[action.payload.type]) {
        console.log(product[action.payload.type], action.payload[action.payload.type])
        return;
      }
      product[action.payload.type] = action.payload[action.payload.type]

      if (state.createOrder.appliedTariffs) {
        if (action.payload.type === "price") {
          state.createOrder.calcCostForTariffTasks = getActiveValidTariffs(state.createOrder.appliedTariffs)
            .map((it) => it.tariffId)
          
        } else {
          const tariffsToAdd = Object.values(state.createOrder.appliedTariffs)
            .filter((t) => t.applyLevel === "ORDER" || t.selectedProducts.includes(action.payload.productId))
            .filter((t) => !t.isDisabled && isAppliedTariffValid(t))
            .map((t) => (t.tariffId))
            .filter((t) => !state.createOrder.calcCostForTariffTasks.includes(t))

          state.createOrder.calcCostForTariffTasks = [...state.createOrder.calcCostForTariffTasks, ...tariffsToAdd]
        }
      }
    },
    setAppliedTariff: (state, action) => {
      const appliedTariff = action.payload
      state.createOrder.appliedTariffs[action.payload.tariffId] = appliedTariff

      if (!appliedTariff.isDisabled) {
        if (isAppliedTariffValid(appliedTariff)) {
          if (!state.createOrder.calcCostForTariffTasks.includes(appliedTariff.tariffId)) {
            state.createOrder.calcCostForTariffTasks = [...state.createOrder.calcCostForTariffTasks, appliedTariff.tariffId]
          }
        } else {
          state.createOrder.appliedTariffs[appliedTariff.tariffId].cost = undefined
        }
      }
    },
    setTasksForCompleteCalculation: (state) => {
      state.createOrder.calcCostForTariffTasks = getActiveValidTariffs(state.createOrder.appliedTariffs).map((t) => t.tariffId)
    }
  },

  extraReducers(builder) {
    builder
      .addCase(calcOrderServiceCostForCreateForm.fulfilled, (state, action) => {
        console.log(action.payload)

        const calculatedOrder = action.payload.order;
        const tariffId = action.payload.order.appliedTariffs?.[0]?.tariffId;
        if (tariffId) {
          const cost = action.payload.cost
          state.createOrder.appliedTariffs[tariffId].cost = cost

          calculatedOrder.products.forEach((product) => {
            state.createOrder.orderProducts[product.productId] = {
              ...state.createOrder.orderProducts[product.productId],
              ...getFeatures(product)
            }
          })
          if (calculatedOrder.products.length === Object.keys(state.createOrder.orderProducts)) {
            state.createOrder = {
              ...state.createOrder,
              ...getFeatures(calculatedOrder)
            }
          }
          state.createOrder.totalCost = undefined
          state.createOrder.calcCostForTariffTasks = state.createOrder.calcCostForTariffTasks.filter((t) => (t != tariffId));
        }
        state.progress = false;
      })
      .addCase(calcOrderServiceCostForCreateForm.pending, (state, action) => {
        state.progress = true
      })
      .addCase(calcOrderServiceCostForCreateForm.rejected, (state, action) => {
        console.log(action)
        state.createOrder.calcCostForTariffTasks = []
        state.status = {
          message: `Не удалось рассчитать стоимость`,
          code: action.payload.code,
        }
        state.progress = false;
      })

      .addCase(calcOrderCostForCreateForm.fulfilled, (state, action) => {
        console.log(action.payload)

        const results = action.payload.calculatedTariffs
        const tariffIds = []
        results.forEach((result) => {
          const tariffId = result.order.appliedTariffs?.[0]?.tariffId;
          if (tariffId) {
            tariffIds.push(tariffId)
            const cost = result.cost
            state.createOrder.appliedTariffs[tariffId].cost = cost 
          }
        })

        const calculatedOrder = action.payload.complexOrder;
        calculatedOrder.products.forEach((product) => {
          state.createOrder.orderProducts[product.productId] = {
            ...state.createOrder.orderProducts[product.productId],
            ...getFeatures(product)
          }
        })

        if (calculatedOrder.products.length === Object.keys(state.createOrder.orderProducts).length) {
          state.createOrder = {
            ...state.createOrder,
            ...getFeatures(calculatedOrder)
          }
        }

        if (getActiveValidTariffs(state.createOrder.appliedTariffs).length === results.length) {
          state.createOrder.totalCost = action.payload.totalCost
        } else {
          state.createOrder.totalCost = undefined
        }

        state.createOrder.calcCostForTariffTasks = state.createOrder.calcCostForTariffTasks.filter((t) => (!tariffIds.includes(t)));
        state.progress = false;
      })
      .addCase(calcOrderCostForCreateForm.pending, (state, action) => {
        state.progress = true
      })
      .addCase(calcOrderCostForCreateForm.rejected, (state, action) => {
        console.log(action)
        state.createOrder.calcCostForTariffTasks = []
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
  }
});

export const { resetStatus, initCreateOrder, setOrderProductFeature, initAppliedTariffs, setAppliedTariff } = orderCreateSlice.actions;

export const selectCreateOrder = (state) => state.orderCreate.createOrder;
export default orderCreateSlice.reducer;