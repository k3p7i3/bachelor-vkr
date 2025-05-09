import { Divider, Grid2, Typography, Button } from "@mui/material";
import EditableOrderProductCard from "./EditableOrderProductCard";
import ApplyTarifOrderForm from "./ApplyTariffOrderForm";
import CenteredLargeBox from "../../../component/ui/CenteredLargeBox";
import { useNavigate } from "react-router-dom";
import { useEffect } from "react";
import SelectedAgentCard from "../../agent/SelectedAgentCard";
import useOrderCreateState from "../../../hook/useOrderCreateState";
import { useDispatch, useSelector } from "react-redux";
import { selectCreateOrder } from "../orderCreateSlice";
import { createOrder } from "../orderCreateSlice";

function CreateOrderForm() {
  const createOrderState = useOrderCreateState();
  const clearOrderState = useSelector(selectCreateOrder)

  const selectedAgent =createOrderState?.selectedAgent
  const products = createOrderState?.products
  const orderProducts = createOrderState?.orderProducts
  const tariffs = createOrderState?.tariffs
  const appliedTariffs = createOrderState?.appliedTariffs

  const navigate = useNavigate();
  const dispatch = useDispatch();

  useEffect(() => {
    if (!products) {
      navigate('/cart');
    }
  }, [products, navigate])

  const onCreateOrderButtonClick = () => {
    dispatch(createOrder({ order: clearOrderState }))
  }

  return <CenteredLargeBox>

    <Typography variant="h4">Оформить заказ</Typography>

    {selectedAgent && <SelectedAgentCard userId={createOrderState?.userId} selectedAgent={selectedAgent}/>}

    <Divider/>

    <Typography variant="h5">Отредактировать характеристики товаров</Typography>

    <Grid2 container spacing={3} rowSpacing={3}>
      {products && products.map((product) => 
        (
          <Grid2 size={{ xs: 12, sm: 12, md: 6 }} key={"grid_" + product.id}>
            <EditableOrderProductCard
              key={product.id}
              product={product} 
              orderProduct={orderProducts[product.id]}
            />
          </Grid2>
        )
      )}
    </Grid2>

    <Divider/>

    <Typography variant="h5">Оформить услуги</Typography>
    
    {appliedTariffs && tariffs?.map((tariff) => 
      <ApplyTarifOrderForm
        key={tariff.tariffId}
        tariff={tariff} appliedTariff={appliedTariffs?.[tariff.tariffId]}
        products={products} orderProducts={orderProducts}
      />)}

    <Button onClick={onCreateOrderButtonClick}>
      Оформить заказ
    </Button>
  </CenteredLargeBox>

};

export default CreateOrderForm;