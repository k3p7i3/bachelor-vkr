import { createSelector } from "@reduxjs/toolkit";
import { selectAgentSelection } from "../agent/agentSlice";
import { selectTariffsForOrder } from "../tariff/tariffSlice";
import { selectCreateOrder, selectOrderCreateCartProducts } from "./orderCreateSlice";
import { selectCurrentOrder } from "./orderSlice";
import { selectOrderProducts } from "../product/productSlice";
import { Tsunami } from "@mui/icons-material";

export const selectCreateOrderInfo = createSelector(
  [selectCreateOrder, selectOrderProducts, selectAgentSelection, selectTariffsForOrder],
  (createOrder, products, agent, tariffs) => {
    if (!createOrder) return null;

    if (!products || createOrder.id != products.orderId) 
      return createOrder;
  
    if (!agent || agent.agentId != createOrder.agentId) {
      return {
        ...createOrder,
        products: products.products
      }
    }


    if (!tariffs || agent.agentId != tariffs?.agentId) {
      return {
        ...createOrder,
        products: products.products,
        selectedAgent: agent
      }
    }

    return {
      ...createOrder,
      products: products.products,
      selectedAgent: agent,
      tariffs: tariffs?.tariffs
    };
  }
);

export const selectOrderInfo = createSelector(
  [selectCurrentOrder, selectOrderProducts, selectTariffsForOrder],
  (order, products, tariffs) => {
    if (!order) return null;

    if (!products || products.orderId != order.id) {
      return order;
    }

    if (!tariffs || tariffs.agentId != order.agentId) {
      return {
        ...order,
        products: products.products
      }
    }

    return {
      ...order,
      products: products.products,
      tariffs: tariffs?.tariffs
    }
  }
)