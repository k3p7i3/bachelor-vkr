import { useDispatch, useSelector } from "react-redux";
import { useParams } from "react-router-dom";
import { useEffect, useRef } from "react";
import { fetchOrder } from "../feature/order/orderSlice";
import { fetchProductsForOrder } from "../feature/product/productSlice";
import { selectOrderInfo } from "../feature/order/orderSelectors";
import { fetchTariffsForOrder } from "../feature/tariff/tariffSlice";
import OrderCard from "../feature/order/OrderCard";

function useCurrentOrder() {
  const dispatch = useDispatch();
  const order =  useSelector(selectOrderInfo);
  const params = useParams();
  const orderId = useRef();

  useEffect(() => {
    console.log('useEffect', order)
    if (params.orderId != orderId.current) {
      dispatch(fetchOrder({ orderId: params.orderId }));
      orderId.current = params.orderId;
    } else {
      if (!order.products) {
        dispatch(fetchProductsForOrder({ productIds: Object.keys(order.orderProducts), orderId: order.id}));
      } else {
        if (!order.tariffs) {
          dispatch(fetchTariffsForOrder({ agentId: order.agentId }));
        }
      }
    }
  }, [dispatch, params, orderId, order]);
  
  return order;
};

export default useCurrentOrder;