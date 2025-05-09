import { Typography } from "@mui/material"
import { mapUnitMeasurement } from "../tariff/TariffMapUtils"

function FeatureText({feature, fieldNames, numericType}) {
  return <Typography>
    {feature 
      ? fieldNames.map((name) => feature[name]).join("x") + " " + 
        mapUnitMeasurement[feature[numericType == "PRICE" ? "currency" : "unit"]] 
      : "неизвестен"
    }
  </Typography>
};

export default FeatureText;
