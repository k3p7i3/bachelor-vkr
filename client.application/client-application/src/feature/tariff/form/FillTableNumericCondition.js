import { Typography, Stack, TextField, styled, Button } from "@mui/material";
import { useEffect, useState } from "react";
import { mapUnitMeasurement } from "../TariffMapUtils";
import DenseTextField from "../../../component/ui/DenseTextField";
import { NumericConditionTableCell } from "../TariffConditionTableCell";

export default function FillTableNumericCondition({ condition }) {
  const floatRegex = /^\d*\.?\d*$/;
  const [isActive, setIsActive] = useState(true);

  const measurementUnit = mapUnitMeasurement[condition.measurementUnit] || '';

  const [minLimit, setMinLimit] = useState(condition.minLimit || '');
  const [isMinValid, setIsMinValid] = useState(true);
   
  const [maxLimit, setMaxLimit] = useState(condition.maxLimit || '');
  const [isMaxValid, setIsMaxValid] = useState(true);

  useEffect(() => {
    condition.minLimit  = isMinValid ? minLimit : '';
    condition.maxLimit = isMaxValid ? maxLimit : '';
  }, [minLimit, isMinValid, maxLimit, isMaxValid, condition]);

  const handleMinLimitChange = (e) => {
    const inputValue = e.target.value;
    setIsMinValid(floatRegex.test(inputValue));
    setMinLimit(inputValue)
  };

  const handleMaxLimitChange = (e) => {
    const inputValue = e.target.value;
    setIsMaxValid(floatRegex.test(inputValue));
    setMaxLimit(inputValue)
  };

  const changeIsActiveOnEnter = (e) => {
    if (e.key === 'Enter') {
      setIsActive(!isActive);
    } 
  }

  if (isActive) {
    return <Stack direction="row" spacing={1}>
      <DenseTextField
        value={minLimit}
        onChange={handleMinLimitChange}
        label="от"
        error={!isMinValid}
        size="small"
        onKeyPress={changeIsActiveOnEnter}
      />
      <Typography variant="body1">—</Typography>
      <DenseTextField
        value={maxLimit}
        onChange={handleMaxLimitChange}
        label="до"
        error={!isMaxValid}
        size="small"
        onKeyPress={changeIsActiveOnEnter}
      />
      <Typography>{measurementUnit}</Typography>
    </Stack>;
  } else {
    return <Button
      disableRipple
      color="primaryText" 
      sx={{textTransform: 'none'}}
      onDoubleClick={(e) => setIsActive(!isActive)}
    >
      <NumericConditionTableCell condition={condition}/>
    </Button>;
  };
};