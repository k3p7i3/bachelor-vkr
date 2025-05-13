import { Button, Stack, Typography } from "@mui/material";
import { useState } from "react";

import CenteredMediumBox from "../../component/ui/CenteredMediumBox";
import ProductCard from "./ProductCard"
import useCart from "../../hook/useCart";
import useSelectedAgent from "../../hook/useSelectedAgent";
import { useDispatch } from "react-redux";
import { initCreateOrder } from "../order/orderSlice";
import { setOrderProducts } from "./productSlice";
import { useNavigate } from "react-router-dom";
import SelectedAgentCard from "../agent/SelectedAgentCard";

function CartProductsFeed() {
  const cart = useCart();
  const selectedAgent = useSelectedAgent();

  const dispatch = useDispatch();
  const navigate = useNavigate();

  const [selected, setSelected] = useState([]);

  const changeSelected = ({ productId, checked}) => {
    if (checked) {
      setSelected([...selected, productId])
    } else {
      setSelected(
        selected.filter((v) => (v !== productId))
      )
    }
  }

  const onButtonClick = () => {
    const products = cart.products.filter((product) => (
      selected.includes(product.id)
    ))
    dispatch(setOrderProducts({orderId: undefined, products: products.map((it) => it.product)}))
    dispatch(initCreateOrder({
      userId: cart.userId,
      agentId: selectedAgent?.agentId,
      products: products
    }));
    navigate('/order/create');
  }

  return <Stack spacing={4}>
    <CenteredMediumBox>
      <Typography variant="h4">Корзина</Typography>
    </CenteredMediumBox>

    {cart && cart.products.map((product) => (
      <ProductCard
        key={product.id}
        cartProduct={product}
        changeSelected={changeSelected}
      />
    ))}

    {cart && selectedAgent && <SelectedAgentCard userId={cart.userId} selectedAgent={selectedAgent}/>}

    <Button onClick={onButtonClick}>Оформить заказ</Button>
  </Stack>
};

export default CartProductsFeed;