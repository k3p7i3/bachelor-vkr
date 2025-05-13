import { useDispatch, useSelector } from "react-redux";
import { useEffect } from "react";
import { getUserCart, selectCart } from "../feature/product/productSlice";
import { selectAuthUser } from "../feature/user/userSlice";

function useCart() {
  const authUser = useSelector(selectAuthUser);
  const cart = useSelector(selectCart);
  const dispatch = useDispatch();

  useEffect(() => {
    if (authUser && !cart) {
        dispatch(getUserCart({userId: authUser.id}));
    }
  }, [authUser, cart, dispatch]);

  return cart;
}

export default useCart;