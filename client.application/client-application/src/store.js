import { configureStore } from '@reduxjs/toolkit';
import authReducer from "./feature/authSlice";
import userReducer from './feature/user/userSlice';
import agentReducer from './feature/agent/agentSlice';
import productReducer from './feature/product/productSlice';
import tariffReducer from './feature/tariff/tariffSlice';
import orderCreateReducer from './feature/order/orderCreateSlice';
import orderReducer from './feature/order/orderSlice';

const store = configureStore({
  reducer: {
    auth: authReducer,
    user: userReducer,
    agent: agentReducer,
    tariff: tariffReducer,
    product: productReducer,
    orderCreate: orderCreateReducer,
    order: orderReducer
  }
})
export default store;