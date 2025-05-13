import { Typography, Stack, Divider, Grid2, Card, Chip, Box, Button } from "@mui/material";
import CenteredLargeBox from "../../../component/ui/CenteredLargeBox";
import useCurrentOrder from "../../../hook/useCurrentOrder";
import StaticOrderProductCard from "./StaticOrderProductCard"
import StaticOrderFeaturesCard from "./StaticOrderFeaturesCard";
import StaticAppliedTariff from "./StaticAppliedTariff";
import FeatureText from "../FeatureText";
import SelectedAgentCard from "../../agent/SelectedAgentCard";
import StaticPaymentCard from "./StaticPaymentCard";
import { useState } from "react";
import YooWidget from "../YooKassaPaymentWidget";
import { useDispatch, useSelector } from "react-redux";
import { resetPaymentToken, selectConfirmationToken } from "../orderSlice";

function OrderCard() {
  const order = useCurrentOrder();
  const orderProducts = order?.orderProducts
  const products = order?.products
  const selectedAgent = order?.selectedAgent

  const dispatch = useDispatch();
  const confirmationToken = useSelector(selectConfirmationToken);
  const [paymentWidgetShown, setPaymentWidgetShown] = useState(false);

  return <CenteredLargeBox>
    {order && 
      <Stack direction="row" justifyContent="space-between" alignItems='center'>
        <Typography variant="h4">Заказ {order.id}</Typography>
        <Chip label='Заказ создан' color='primary' variant="outlined"/>
      </Stack>
    }

    <Divider/>

    <Typography variant="h5">Товары</Typography>

    <Grid2 container spacing={3} rowSpacing={3}>
      {products && products.map((product) => 
        (
          <Grid2 size={{ xs: 12, sm: 12, md: 6 }} key={"grid_" + product.id}>
            <StaticOrderProductCard
              key={product.id}
              product={product} 
              orderProduct={orderProducts[product.id]}
            />
          </Grid2>
        )
      )}
    </Grid2>

    <Typography variant="h5">Измерения всего груза</Typography>

    <Card sx={{ p: 2 }}>
      {order &&
        <StaticOrderFeaturesCard order={order}/>}
    </Card>

    <Divider/>

    <Typography variant="h5">Оформленные услуги</Typography>
    
    {order?.appliedTariffs && order.tariffs?.map((tariff) => 
      <StaticAppliedTariff
        key={tariff.tariffId}
        tariff={tariff} appliedTariff={order.appliedTariffs?.[tariff.tariffId]}
        products={products} orderProducts={orderProducts}
      />)}

    <Divider/>

    <Typography variant="h5">Счета на оплату</Typography>
    {order?.tariffs && order?.payments?.map((payment) => (
      <StaticPaymentCard 
        orderId={order.id}
        tariffs={order.tariffs} 
        payment={payment} 
        setPaymentWidgetShown={setPaymentWidgetShown}
      />
    ))}

    <Stack direction='row' justifyContent='space-between' alignItems='center'>
      {selectedAgent && <SelectedAgentCard selectedAgent={selectedAgent} userId={order.userId} canDelete={false}/>}

      {order?.totalCost &&   
        <Stack>
          <Typography fontWeight="bold" textAlign="end"> Итоговая цена </Typography>
          <FeatureText 
            feature={order.totalCost} 
            fieldNames={['value']} 
            numericType={'PRICE'}
            style={{ fontSize: '1.4rem', color: 'var(--mui-palette-primary-main)', textAlign: 'end'}}
          />
          {order?.paidAmount &&
            <Box display="flex" gap={1}>
              <Typography color="textSecondary">Оплачено: </Typography>
              <FeatureText 
                feature={order.paidAmount} 
                fieldNames={['value']} 
                numericType={'PRICE'}
                style={{ color: 'var(--mui-palette-text-secondary)'}}
              />
            </Box>  
          }  
        </Stack>
  
      }

    </Stack>

    {(confirmationToken && paymentWidgetShown)
      ? <YooWidget 
          config={{
            confirmation_token: confirmationToken,
            customization: {
              modal: true
            },
            error_callback: (error) => {

            }
          }}
          onFail={() => {
            alert("Выплата не прошла");
            dispatch(resetPaymentToken());
          }}
          onSuccess={() => {
            setPaymentWidgetShown(false);
            dispatch(resetPaymentToken())
            window.location.reload();
          }}
          onModalClose={() => {
            setPaymentWidgetShown(false);
          }}
        /> 
      : ''
    }
  </CenteredLargeBox>;
}

export default OrderCard;