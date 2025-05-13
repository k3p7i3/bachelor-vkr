import { Stack,  Typography, Divider } from "@mui/material";
import { useState } from "react";
import { useDispatch } from "react-redux";
import EditableFeatureField from "./EditableFeatureField";
import { setOrderProductFeature } from "../orderSlice";
import OrderProductCardBase from "../OrderProductCardBase";

function EditableProductFeatures({orderProduct}) {
  const weight = orderProduct.weight
  const boxVolume = orderProduct.boxVolume
  const volume = orderProduct.volume
  const price = orderProduct.price
  // const [weight, setWeight] = useState(orderProduct.weight || '');
  // const [boxVolume, setBoxVolume] = useState(orderProduct.boxVolume || '');
  // const [volume, setVolume] = useState(orderProduct.volume || '');
  // const [price, setPrice] = useState(orderProduct.price || '');

  const dispatch = useDispatch();

  const createSetFeatureCallback = (type) => {
    return (e) => {
      dispatch(setOrderProductFeature({
        productId: orderProduct.productId,
        type: type,
        [type]: (e || undefined)
      }))
    }
  }

  return <>
    <Divider>Редактировать характеристики</Divider>

    <Stack spacing={1}>
      <Stack direction="row" sx={{alignItems: "center"}} spacing={1}>
        <Typography fontWeight="bold">Вес: </Typography>
        <EditableFeatureField
          fieldNames={["value"]}
          feature={weight} 
          setFeature={createSetFeatureCallback("weight")} 
          numericType="WEIGHT"
        />
      </Stack>

      <Stack direction="row" sx={{alignItems: "center"}} spacing={1}>
        <Typography fontWeight="bold">Габариты: </Typography>
        <EditableFeatureField
            key="box"
            fieldNames={["length", "width", "height"]}
            feature={boxVolume} 
            setFeature={createSetFeatureCallback("boxVolume")} 
            numericType="LENGTH"
          />
      </Stack>

      <Stack direction="row" sx={{alignItems: "center"}} spacing={1}>
        <Typography fontWeight="bold">Объем: </Typography>
        <EditableFeatureField
            key="volume"
            fieldNames={["value"]}
            feature={volume}
            setFeature={createSetFeatureCallback("volume")} 
            numericType="VOLUME"
          />
      </Stack>

      <Stack direction="row" sx={{alignItems: "center"}} spacing={1}>
        <Typography fontWeight="bold">Цена: </Typography>
        <EditableFeatureField
          fieldNames={["value"]}
          feature={price} 
          setFeature={createSetFeatureCallback("price")} 
          numericType="PRICE"
        />
      </Stack>
    </Stack>
  </>
};

function EditableOrderProductCard({product, orderProduct}) {
  return <OrderProductCardBase product={product} orderProduct={orderProduct}
    featuresCard={<EditableProductFeatures orderProduct={orderProduct}/>}
  />
}

export default EditableOrderProductCard;