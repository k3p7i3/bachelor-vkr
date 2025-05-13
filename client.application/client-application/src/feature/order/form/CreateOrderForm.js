import { Divider, Grid2, Typography, Button, Card, Stack } from "@mui/material";
import EditableOrderProductCard from "./EditableOrderProductCard";
import ApplyTarifOrderForm from "./ApplyTariffOrderForm";
import CenteredLargeBox from "../../../component/ui/CenteredLargeBox";
import { useNavigate } from "react-router-dom";
import { useEffect } from "react";
import SelectedAgentCard from "../../agent/SelectedAgentCard";
import useCurrentOrder from "../../../hook/useCurrentOrder";
import { useDispatch, useSelector } from "react-redux";
import { selectCurrentOrder } from "../orderSlice";
import { createOrder } from "../orderSlice";
import EditableOrderFeatures from "./EditableOrderFeatures";
import { calcOrderCost } from "../orderSlice";
import FeatureText from "../FeatureText";

function CreateOrderForm() {
  const createOrderState = useCurrentOrder();
  const clearOrderState = useSelector(selectCurrentOrder)

  const orderId = createOrderState?.id
  const selectedAgent =createOrderState?.selectedAgent
  const products = createOrderState?.products
  const orderProducts = createOrderState?.orderProducts
  const tariffs = createOrderState?.tariffs
  const appliedTariffs = createOrderState?.appliedTariffs

  const navigate = useNavigate();
  const dispatch = useDispatch();

  useEffect(() => {
    if (orderId) {
      navigate('/order/' + orderId);
    } else {
      if (!products) {
        navigate('/cart');
      }
    }
  }, [products, orderId, navigate]);


  const onCalcCostClick = () => {
    dispatch(calcOrderCost({ order: createOrderState }))
  }

  const onCreateOrderButtonClick = () => {
    dispatch(createOrder({ order: clearOrderState }))
  }

  return <CenteredLargeBox>
    <Typography variant="h4">Оформить заказ</Typography>

    {selectedAgent && <SelectedAgentCard userId={createOrderState?.userId} selectedAgent={selectedAgent} canDelete={false}/>}

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

    <Typography variant="h5">Характеристики всего груза</Typography>

    {createOrderState && 
      <EditableOrderFeatures order={createOrderState}/>}

    <Divider/>

    <Typography variant="h5">Оформить услуги</Typography>
    
    {appliedTariffs && tariffs?.map((tariff) => 
      <ApplyTarifOrderForm
        key={tariff.tariffId}
        tariff={tariff} appliedTariff={appliedTariffs?.[tariff.tariffId]}
        products={products} orderProducts={orderProducts}
      />)}

    <Card sx={{p: 2, display: 'flex', justifyContent: 'space-evenly'}}>
      {createOrderState?.totalCost && 
        <Stack direction="row" spacing={2} sx={{alignItems: 'center'}}>
          <Typography variant="h6">Стоимость всего заказа: </Typography>
          <FeatureText 
            feature={createOrderState.totalCost} 
            fieldNames={['value']} 
            numericType={'PRICE'}
            style={{ fontSize: '1.4rem', color: 'var(--mui-palette-primary-main)'}}
          />
        </Stack>
      }

      <Button variant="outlined" onClick={onCalcCostClick}>
        Рассчитать стоимость
      </Button>
    </Card>

    <Button variant= "contained" onClick={onCreateOrderButtonClick}>
      Оформить заказ
    </Button>
  </CenteredLargeBox>

};

export default CreateOrderForm;