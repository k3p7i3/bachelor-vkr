import { Typography, Stack } from "@mui/material";
import FeatureText from "../FeatureText";
import OrderProductCardBase from "../OrderProductCardBase";


function ProductFeatures({orderProduct}) {
  return <Stack spacing={1}>
    <Stack direction="row" sx={{alignItems: "center"}} spacing={1}>
      <Typography fontWeight="bold">Вес: </Typography>
      <FeatureText feature={orderProduct.weight} fieldNames={["value"]} numericType={"WEIGHT"}/>
    </Stack>

    <Stack direction="row" sx={{alignItems: "center"}} spacing={1}>
      <Typography fontWeight="bold">Объем (по габаритам): </Typography>
      <FeatureText feature={orderProduct.boxVolume} fieldNames={["length", "width", "height"]} numericType={"BOX_VOLUME"}/>
    </Stack>

    <Stack direction="row" sx={{alignItems: "center"}} spacing={1}>
      <Typography fontWeight="bold">Объем (абсолютный): </Typography>
      <FeatureText feature={orderProduct.volume} fieldNames={["value"]} numericType={"VOLUME"}/>
    </Stack>

    <Stack direction="row" sx={{alignItems: "center"}} spacing={1}>
      <Typography fontWeight="bold">Цена: </Typography>
      <FeatureText feature={orderProduct.price} fieldNames={["value"]} numericType={"PRICE"}/>
    </Stack>
  </Stack>
};

export default function StaticOrderProductCard({product, orderProduct}) {
  return <OrderProductCardBase
    product={product} orderProduct={orderProduct} featuresCard={<ProductFeatures orderProduct={orderProduct}/>}
  />
};