import { useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { Typography, Grid2, Divider, Card, Stack, Button } from "@mui/material";
import useCurrentOrder from "../../../hook/useCurrentOrder";
import { selectCurrentOrder, calcOrderCost } from "../orderSlice";
import EditableOrderProductCard from "../form/EditableOrderProductCard";
import EditableOrderFeatures from "../form/EditableOrderFeatures";
import ApplyTarifOrderForm from "../form/ApplyTariffOrderForm";
import FeatureText from "../FeatureText";
import CenteredLargeBox from "../../../component/ui/CenteredLargeBox";
import AddPaymentForm from "./AddPaymentForm";

function EditableOrderCard() {
  const order = useCurrentOrder();
  const clearOrderState = useSelector(selectCurrentOrder);

  const orderId = order?.id
  const products = order?.products
  const orderProducts = order?.orderProducts
  const tariffs = order?.tariffs
  const appliedTariffs = order?.appliedTariffs

  const navigate = useNavigate();
  const dispatch = useDispatch();

  const onCalcCostClick = () => {
    dispatch(calcOrderCost({ order: order }))
  }

  const onCreateOrderButtonClick = () => {
    // dispatch(createOrder({ order: clearOrderState }))
  }

  return <CenteredLargeBox>
    {order &&
      <>
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
    
        <EditableOrderFeatures order={order}/>
    
        <Divider/>
    
        <Typography variant="h5">Оформленные услуги</Typography>
        
        {appliedTariffs && tariffs?.map((tariff) => 
          <ApplyTarifOrderForm
            key={tariff.tariffId}
            tariff={tariff} appliedTariff={appliedTariffs?.[tariff.tariffId]}
            products={products} orderProducts={orderProducts}
          />)}
    
        <Card sx={{p: 2, display: 'flex', justifyContent: 'space-evenly'}}>
          {order?.totalCost && 
            <Stack direction="row" spacing={2} sx={{alignItems: 'center'}}>
              <Typography variant="h6">Стоимость всего заказа: </Typography>
              <FeatureText 
                feature={order.totalCost} 
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


        {tariffs &&
          <AddPaymentForm tariffs={tariffs}/>
        }
    
        <Button variant= "contained" onClick={onCreateOrderButtonClick}>
          Сохранить изменения
        </Button>
      </>
    }
  </CenteredLargeBox>
};

export default EditableOrderCard;