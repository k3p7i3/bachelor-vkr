import { Typography } from "@mui/material"
import { mapUnitMeasurement } from "../tariff/TariffMapUtils"

function FeatureText({feature, fieldNames, numericType, style = {}}) {
  return <Typography sx={style}>
    {feature 
      ? fieldNames.map((name) => feature[name].toFixed(2)).join("x") + " " + 
        mapUnitMeasurement[feature[numericType == "PRICE" ? "currency" : "unit"]] 
      : "неизвестен"
    }
  </Typography>
};

export default FeatureText;
