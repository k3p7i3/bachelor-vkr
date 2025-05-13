import { useEffect, useRef } from "react";
import { useParams } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { fetchTariffsForOrder } from "../feature/tariff/tariffSlice";
import { fetchSelectedBriefAgent } from "../feature/agent/agentSlice";
import { selectOrderInfo } from "../feature/order/orderSelectors";
import { fetchProductsForOrder } from "../feature/product/productSlice";
import { initAppliedTariffs, fetchOrder } from "../feature/order/orderSlice";

function useCurrentOrder() {
  const dispatch = useDispatch();
  const order = useSelector(selectOrderInfo);
  const params = useParams();
  const orderId = useRef();

  useEffect(() => {
    console.log('useEffect', order)
    if (params.orderId != orderId.current) {
      if (params.orderId) {
        dispatch(fetchOrder({ orderId: params.orderId }));
      }
      orderId.current = params.orderId;
    } else {
      if (order) {
        if (!order.products) {
          dispatch(fetchProductsForOrder({ productIds: Object.keys(order.orderProducts), orderId: order.id}));
        } else {
          if (!order.tariffs) {
            dispatch(fetchTariffsForOrder({ agentId: order.agentId }));
          } else {
            if (!order.appliedTariffs) {
              dispatch(initAppliedTariffs({tariffs: order.tariffs}))
            } else {
              const userId = order?.userId
              if (!order.selectedAgent && userId) {
                dispatch(fetchSelectedBriefAgent({userId: userId}))
              }
            }
          }
        }
      }

    }
  }, [dispatch, params, orderId, order]);

  
  return order;
};

export default useCurrentOrder;