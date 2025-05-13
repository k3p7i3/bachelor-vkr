import { Stack, Typography } from "@mui/material";
import FeatureText from "../FeatureText";

function StaticOrderFeaturesCard({order}) {
  return <Stack spacing={1}>
    <Stack direction="row" sx={{alignItems: "center"}} spacing={1}>
      <Typography fontWeight="bold">Вес: </Typography>
      <FeatureText feature={order.weight?.original} fieldNames={["value"]} numericType={"WEIGHT"}/>
    </Stack>

    <Stack direction="row" sx={{alignItems: "center"}} spacing={1}>
      <Typography fontWeight="bold">Объем (по габаритам): </Typography>
      <FeatureText feature={order.boxVolume?.original} fieldNames={["length", "width", "height"]} numericType={"BOX_VOLUME"}/>
    </Stack>

    <Stack direction="row" sx={{alignItems: "center"}} spacing={1}>
      <Typography fontWeight="bold">Объем (абсолютный): </Typography>
      <FeatureText feature={order.volume?.original} fieldNames={["value"]} numericType={"VOLUME"}/>
    </Stack>

    <Stack direction="row" sx={{alignItems: "center"}} spacing={1}>
      <Typography fontWeight="bold">Цена: </Typography>
      <FeatureText feature={order.price?.original} fieldNames={["value"]} numericType={"PRICE"}/>
      {order.price?.normalized && 
        <FeatureText feature={order.price?.normalized} fieldNames={["value"]} numericType={"PRICE"}/>}
    </Stack>
  </Stack>
}

export default StaticOrderFeaturesCard;
