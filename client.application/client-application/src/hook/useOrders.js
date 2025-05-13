import { useEffect } from "react";
import { selectAuthData } from "../feature/authSlice";
import { fetchAgentOrders, fetchClientOrders } from "../feature/order/orderSlice";
import { useDispatch, useSelector } from "react-redux";
import { selectAuthUser } from "../feature/user/userSlice";
import { selectOrderList } from "../feature/order/orderSelectors";
import { fetchProductsForOrders } from "../feature/product/productSlice";

function useOrders() {
  const dispatch = useDispatch();
  const authData = useSelector(selectAuthData);
  const user  = useSelector(selectAuthUser);
  const orders = useSelector(selectOrderList);

  useEffect(() => {
    if (!orders.orders) {
      if (authData.agentId) {
        dispatch(fetchAgentOrders({ agentId: authData.agentId }))
      } else {
        if (user?.id) {
          dispatch(fetchClientOrders({ clientId: user.id}))
        }
      }
    } else {
      if (!orders.products) {
        dispatch(fetchProductsForOrders({
            productIds: orders.orders.flatMap((ord) => (
              ord.products.map((p) => p.productId)
            ))
        }))
      }
    }
  }, [dispatch, orders, authData, user]);

  return orders;
};

export default useOrders;