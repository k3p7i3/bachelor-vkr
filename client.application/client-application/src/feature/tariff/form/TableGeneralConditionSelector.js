import { useState, useEffect } from "react";
import { Stack, Typography, Grid2, FormControl, InputLabel, Select, MenuItem } from "@mui/material";

import { numericConditionTypesList, numericConditionTypes, numericConditionTypeToMeasurements, features } from "./tariffTableFormMapUtils";

export default function TableGeneralConditionSelector({ title, condition }) {

  const [conditionType, setConditionType] = useState(
    condition?.numericCondition?.measurementType || condition?.typeCondition?.feature || ''
  );
  const [measurement, setMeasurement] = useState(
    condition?.numericCondition?.measurementUnit || ''
  );

  const isMeasurementRequired = numericConditionTypesList.includes(conditionType) && conditionType != "UNITS";

  useEffect(() => {
    condition.type = numericConditionTypesList.includes(conditionType) ? "NUMERIC" : "ENUM";
    if (condition.type == "NUMERIC") {
      condition.numericCondition = {
        measurementType: conditionType,
        measurementUnit: (isMeasurementRequired ? measurement : null)
      };
      condition.typeCondition = null;
    } else {
      condition.typeCondition = { feature: conditionType };
      condition.numericCondition = null;
    };
  }, [conditionType, measurement, isMeasurementRequired, condition]);

  const handleConditionTypeChange = (event) => {
    setConditionType(event.target.value);
    setMeasurement('');
  };

  const handleMeasurementChange = (event) => {
    setMeasurement(event.target.value);
  };

  return <Stack spacing={2}>
    <Typography>{title}</Typography>

    <Grid2 container direction={{ xs: "column", sm: "row"}} spacing={2}>
        <Grid2 container size={{ xs: 12, sm: isMeasurementRequired ? 6 : 12 }}>
          <FormControl fullWidth>
            <InputLabel id="choose_condition_type">Выберите тип условия</InputLabel>

            <Select
              labelId="choose_condition_type"
              id="select-table-condition-type"
              value={conditionType}
              label="Тип ограничения"
              onChange={handleConditionTypeChange}
            >
              {features.length !=0 && features.map((feature) => (
                <MenuItem value={feature.featureId}>{feature.title}</MenuItem>
              ))}

              {numericConditionTypes.map((type) => (
                <MenuItem value={type.value}>{type.label}</MenuItem>
              ))}
            </Select>
          </FormControl>
        </Grid2>

      {isMeasurementRequired &&
        <Grid2 container size={{ xs: 12, sm: 6 }}>
          <FormControl fullWidth>
            <InputLabel id="choose_measurement">{
              conditionType == "PRICE" ? "Выберите валюту" : "Выберите единицу измерения"
            }</InputLabel>

            <Select
              labelId="choose_measurement"
              id="select-table-maesurement"
              value={measurement}
              label="Единица измерения"
              onChange={handleMeasurementChange}
            >
              {numericConditionTypeToMeasurements[conditionType].map((measurement) => (
                <MenuItem value={measurement.value}>{measurement.fullLabel} ({measurement.label})</MenuItem>
              ))}
            </Select>
          </FormControl>
        </Grid2>
      }
      </Grid2>
  </Stack>
};