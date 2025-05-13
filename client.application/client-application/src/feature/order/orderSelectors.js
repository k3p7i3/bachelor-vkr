import { createSelector } from "@reduxjs/toolkit";
import { selectAgentSelection } from "../agent/agentSlice";
import { selectTariffsForOrder } from "../tariff/tariffSlice";
import { selectCurrentOrder, selectOrders } from "./orderSlice";
import { selectOrderListProducts, selectOrderProducts } from "../product/productSlice";

export const selectOrderInfo = createSelector(
  [selectCurrentOrder, selectOrderProducts, selectAgentSelection, selectTariffsForOrder],
  (order, products, agent, tariffs) => {
    if (!order) return null;

    if (!products || order.id != products.orderId) 
      return order;
  
    if (!tariffs || order.agentId != tariffs?.agentId) {
      return {
        ...order,
        products: products.products
      }
    }

    if (!agent || agent.agentId != order.agentId) {
      return {
        ...order,
        products: products.products,
        tariffs: tariffs?.tariffs
      }
    }

    return {
      ...order,
      products: products.products,
      tariffs: tariffs?.tariffs,
      selectedAgent: agent
    };
  }
);

export const selectOrderList = createSelector(
  [selectOrders, selectOrderListProducts],
  (orders, products) => {
    return {
      orders: orders,
      products: products
    }
  }
)