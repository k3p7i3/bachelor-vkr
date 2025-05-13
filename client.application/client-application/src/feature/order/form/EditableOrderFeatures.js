import { Stack, Card, Typography } from "@mui/material";
import { useState } from "react";
import { useDispatch } from "react-redux";
import { setOrderFeature } from "../orderSlice";
import EditableFeatureField from "./EditableFeatureField";
import { original } from "@reduxjs/toolkit";

export default function EditableOrderFeatures({order}) {
  const weight = order.weight?.original
  const boxVolume = order.boxVolume?.original
  const volume = order.volume?.original
  const price = order.price?.original

  const createSetFeatureCallback = (type) => {
    return (e) => {
      dispatch(setOrderFeature({
        type: type,
        [type]: e ? {original: e} : e 
      }))
    }
  }

  const dispatch = useDispatch();

  return <Card sx={{ p: 2 }}>
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
  </Card>
};