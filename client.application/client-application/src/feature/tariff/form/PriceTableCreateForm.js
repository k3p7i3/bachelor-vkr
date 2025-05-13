import {  Stack, Typography, Button } from "@mui/material";
import { useRef, useState } from "react";

import TableGeneralConditionSelector from "./TableGeneralConditionSelector";
import TablePriceMeasurementUnitSelector from "./TablePriceMeasurementUnitSelector";
import FillPriceTableForm from "./FillPriceTableForm";

function SelectPriceTableConditionTypes({ columnsGeneralCondition, rowsGeneralCondition, generalPricePerUnit, setStep }) {
  return <Stack spacing={2}>
    <TableGeneralConditionSelector title="Условие по стобцам" condition={columnsGeneralCondition}/>
    <TableGeneralConditionSelector title="Условие по строкам" condition={rowsGeneralCondition}/>
    <TablePriceMeasurementUnitSelector pricePerUnit={generalPricePerUnit}/>
    <Button onClick={() => {setStep(2)}}>Далее</Button>
  </Stack>
}

export default function PriceTableCreateForm({step, setStep}) {

  const columnsGeneralCondition = useRef({});
  const rowsGeneralCondition = useRef({});
  const generalPricePerUnit = useRef({});

  return <Stack spacing={3}>
    <Typography>Таблица</Typography> 
    {step == 1 && 
      <SelectPriceTableConditionTypes
        columnsGeneralCondition={columnsGeneralCondition.current} 
        rowsGeneralCondition={rowsGeneralCondition.current} 
        generalPricePerUnit={generalPricePerUnit.current}
        setStep={setStep}
      />}
    {step == 2 && 
      <FillPriceTableForm 
        columnsGeneralCondition={columnsGeneralCondition.current} 
        rowsGeneralCondition={rowsGeneralCondition.current} 
        generalPricePerUnit={generalPricePerUnit.current}
        setStep={setStep}
      />
    }
    <Button onClick={() => setStep(step - 1)}>Назад</Button>
  </Stack>
};