import { Stack,  Typography, Divider } from "@mui/material";
import { useState } from "react";
import { useDispatch } from "react-redux";
import EditableFeatureField from "./EditableFeatureField";
import { setOrderProductFeature } from "../orderCreateSlice";
import OrderProductCardBase from "../OrderProductCardBase";

function EditableProductFeatures({orderProduct}) {
  const [weight, setWeight] = useState(orderProduct.weight || '');
  const [boxVolume, setBoxVolume] = useState(orderProduct.boxVolume || '');
  const [volume, setVolume] = useState(orderProduct.volume || '');
  const [price, setPrice] = useState(orderProduct.price || '');

  const dispatch = useDispatch();

  const createFeatureEditIsOverCallback = (type) => {
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
          setFeature={setWeight} 
          numericType="WEIGHT"
          featureEditIsOverCallcack={createFeatureEditIsOverCallback("weight")}
        />
      </Stack>

      <Stack direction="row" sx={{alignItems: "center"}} spacing={1}>
        <Typography fontWeight="bold">Габариты: </Typography>
        <EditableFeatureField
            key="box"
            fieldNames={["length", "width", "height"]}
            feature={boxVolume} 
            setFeature={setBoxVolume} 
            numericType="LENGTH"
            featureEditIsOverCallcack={createFeatureEditIsOverCallback("boxVolume")}
          />
      </Stack>

      <Stack direction="row" sx={{alignItems: "center"}} spacing={1}>
        <Typography fontWeight="bold">Объем: </Typography>
        <EditableFeatureField
            key="volume"
            fieldNames={["value"]}
            feature={volume}
            setFeature={setVolume}
            numericType="VOLUME"
            featureEditIsOverCallcack={createFeatureEditIsOverCallback("volume")}
          />
      </Stack>

      <Stack direction="row" sx={{alignItems: "center"}} spacing={1}>
        <Typography fontWeight="bold">Цена: </Typography>
        <EditableFeatureField
          fieldNames={["value"]}
          feature={price} 
          setFeature={setPrice} 
          numericType="PRICE"
          featureEditIsOverCallcack={createFeatureEditIsOverCallback("price")}
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