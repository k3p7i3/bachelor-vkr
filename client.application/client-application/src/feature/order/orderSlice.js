import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { middleware } from "../middleware";
import { getOrder, postOrder, updateOrder, getClientOrders, getAgentOrders } from "../../api/orderApi";
import { outputMapOrder } from "./createOrderFormMappers";

export const fetchOrder = createAsyncThunk('order/fetchOrder', async (params, thunk) => {
  return await middleware(getOrder, params, thunk, { outputMapper: outputMapOrder });
});

export const fetchClientOrders = createAsyncThunk('order/fetchClientOrders', async (params, thunk) => {
  return await middleware(getClientOrders, params, thunk, { });
});

export const fetchAgentOrders = createAsyncThunk('order/fetchAgentOrders', async (params, thunk) => {
  return await middleware(getAgentOrders, params, thunk, { });
});

const orderSlice = createSlice({
  name: 'order',
  initialState: {
    currentOrder: undefined,
    orders: undefined,
    status: {
      message: undefined,
      code: undefined
    },
    progress: false
  },
  reducers: {
    resetStatus: (state, action) => {
      state.status = {
        message: undefined,
        code: undefined,
      }
    }
  },

  extraReducers(builder) {
      builder
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
  }
});

export const selectCurrentOrder = (state) => state.order.currentOrder;
export const selectOrders = (state) => state.order.orders;

export default orderSlice.reducer;
export const { resetStatus } = orderSlice.actions;