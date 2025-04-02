import { useState, useEffect } from "react";
import { Stack, Typography, Button } from "@mui/material";

import DenseTextField from "../../../../component/ui/DenseTextField";
import { TariffPriceCell } from "../TariffPriceCell";
import { mapUnitMeasurement, mapPerUnitType } from "../TariffMapUtils";

export default function FillTablePricePerUnit({ price }) {
  const floatRegex = /^\d*\.?\d*$/;
  const [isActive, setIsActive] = useState(true);

  const [priceValue, setPriceValue] = useState(price.price.value || '');
  const [isPriceValid, setIsPriceValid] = useState(true);
   
  const priceMeasurementUnit = (
    (price.price.unit ? mapUnitMeasurement[price.price.unit] : '') + mapPerUnitType[price.perUnit.unitType] + 
    (price.perUnit.unit ? mapUnitMeasurement[price.perUnit.unit] : '')
  );

  useEffect(() => {
    price.price.value = isPriceValid ? priceValue : '';
  }, [priceValue, isPriceValid, price]);

  const handlePriceValueChange = (e) => {
    const inputValue = e.target.value;
    setIsPriceValid(floatRegex.test(inputValue));
    setPriceValue(inputValue);
  };

  const changeIsActiveOnEnter = (e) => {
    if (e.key === 'Enter') {
      setIsActive(!isActive);
    };
  };

  if (isActive) {
    return <Stack direction="row" spacing={1}>
      <DenseTextField
        value={priceValue}
        onChange={handlePriceValueChange}
        label="цена"
        error={!isPriceValid}
        size="small"
        onKeyPress={changeIsActiveOnEnter}
      />
      <Typography>{priceMeasurementUnit}</Typography>
    </Stack>;
  } else {
    return <Button
      disableRipple
      color="primaryText" 
      sx={{textTransform: 'none'}}
      onDoubleClick={(e) => setIsActive(!isActive)}
    >
      <TariffPriceCell price={price}/>
    </Button>;
  };
};