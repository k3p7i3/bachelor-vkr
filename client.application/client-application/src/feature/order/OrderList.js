import { Link as RouterLink, useNavigate } from "react-router-dom";
import { Link, Typography, Card, Stack } from "@mui/material";
import CenteredMediumBox from "../../component/ui/CenteredMediumBox";
import useOrders from "../../hook/useOrders";
import CustomCarousel from "../../component/ui/CustomCarousel";

export default function OrderList() {
  const navigate = useNavigate();
  const ordersData = useOrders();
  const orders = ordersData.orders;
  const products = ordersData.products;
  console.log(ordersData);
  return <CenteredMediumBox>


    {orders && orders.map((order) => {
      const orderProducts = products?.filter((p) => order.products.map((it) => it.productId).includes(p.id))
      return <Card key={order.id} sx={{ p: 2 }}>
        <Stack spacing={2}>
          <Typography 
            onClick={() => { navigate('/order/' + order.id)}} 
            sx={{ "&:hover": { color: 'var(--mui-palette-primary-dark)' } }}
            variant="h6"
          >
            Заказ {order.id}
          </Typography>
          {orderProducts && orderProducts.length != 0 &&
            <CustomCarousel items={{large: 7, medium: 5, small: 3}} slidesToSlide={2}>
              {orderProducts.map((product) => {
                return <img src={product.imageUrl} className="mini-carousel-img"/>
              })}
            </CustomCarousel>  
          }
        </Stack>

      </Card>
    })}
  </CenteredMediumBox>
};