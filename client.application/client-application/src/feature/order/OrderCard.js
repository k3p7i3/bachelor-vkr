import { Typography, Stack, Divider, Grid2, Card } from "@mui/material";
import CenteredLargeBox from "../../component/ui/CenteredLargeBox";
import useCurrentOrder from "../../hook/useCurrentOrder";
import OrderProductCard from "./StaticOrderProductCard"
import StaticOrderFeaturesCard from "./StaticOrderFeaturesCard";
import StaticAppliedTariff from "./StaticAppliedTariff";

function OrderCard() {
  const order = useCurrentOrder();
  const orderProducts = order?.orderProducts
  const products = order?.products

  return <CenteredLargeBox>
    {order && <Typography variant="h4">Заказ {order.id}</Typography>}

    <Divider/>

    <Typography variant="h5">Товары</Typography>

    <Grid2 container spacing={3} rowSpacing={3}>
      {products && products.map((product) => 
        (
          <Grid2 size={{ xs: 12, sm: 12, md: 6 }} key={"grid_" + product.id}>
            <OrderProductCard
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

    <Typography variant="h5">Оформить услуги</Typography>
    
    {order?.appliedTariffs && order.tariffs?.map((tariff) => 
      <StaticAppliedTariff
        key={tariff.tariffId}
        tariff={tariff} appliedTariff={order.appliedTariffs?.[tariff.tariffId]}
        products={products} orderProducts={orderProducts}
      />)}
  </CenteredLargeBox>;
}

export default OrderCard;