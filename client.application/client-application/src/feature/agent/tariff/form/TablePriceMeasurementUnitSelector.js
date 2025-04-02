import { Stack, Typography, Grid2, FormControl, InputLabel, Select, MenuItem } from "@mui/material";
import { useState, useEffect } from "react";
import { numericConditionTypeToMeasurements, perUnitTypeList } from "./tariffTableFormMapUtils";

export default function TablePriceMeasurementUnitSelector({ pricePerUnit }) {
  const [currency, setCurrency] = useState(pricePerUnit?.price?.unit || '');
  const [perUnit, setPerUnit] = useState(pricePerUnit?.perUnit?.unitType || '');
  const [perUnitMeasurement, setPerUnitMeasurement] = useState(pricePerUnit?.perUnit?.unit ||'');

  const isMeasurementRequired =  (
    perUnit == "WEIGHT" ||  perUnit == "VOLUME" // || perUnit == "DENSITY"
  );

  useEffect(() => {
    pricePerUnit.price = {
      unit: currency
    };
    pricePerUnit.perUnit = {
      unitType: perUnit,
      unit: isMeasurementRequired ? perUnitMeasurement : null
    };
  }, [currency, perUnit, perUnitMeasurement, isMeasurementRequired, pricePerUnit]);


  return <Stack spacing={2}>
    <Typography>Тип цен для таблицы</Typography>

    <Grid2 container spacing={2} direction="row">
      <Grid2 container size={{ xs: 12, sm: isMeasurementRequired ? 4 : 6 }}>
        <FormControl fullWidth>
          <InputLabel id="choose_currency">Выберите валюту для цен</InputLabel>
          <Select
            labelId="choose_currency"
            id="select-table-price-currency"
            value={currency}
            label="Валюта"
            onChange={(e) => {setCurrency(e.target.value)}}
          >
            {numericConditionTypeToMeasurements["PRICE"].map((currency) => (
              <MenuItem value={currency.value}>{currency.fullLabel} ({currency.label})</MenuItem>
            ))}
          </Select>
        </FormControl>
      </Grid2>

      <Grid2 container size={{ xs: 12, sm: isMeasurementRequired ? 4 : 6 }}>
        <FormControl fullWidth>
          <InputLabel id="choose_per_unit_type">Выберите категорию для цены</InputLabel>
            <Select
              labelId="choose_per_unit_type"
              id="select-table-per-unit-type"
              value={perUnit}
              label="Тип per unit"
              onChange={(e) => {setPerUnit(e.target.value)}}
            >
              {perUnitTypeList.map((perUnitType) => (
                <MenuItem value={perUnitType.value}>{perUnitType.label}</MenuItem>
              ))}
            </Select>
        </FormControl>
      </Grid2>

      {isMeasurementRequired &&
        <Grid2 container size={{ xs: 12, sm: isMeasurementRequired ? 4 : 6 }}>
          <FormControl fullWidth>
            <InputLabel id="choose_per_unit_measurement">Выберите единицу измерения</InputLabel>
              <Select
                labelId="choose_per_unit_measurement"
                id="select-table-per-unit-measurement"
                value={perUnitMeasurement}
                label="Тип per unit"
                onChange={(e) => {setPerUnitMeasurement(e.target.value)}}
              >
                {numericConditionTypeToMeasurements[perUnit].map((measurement) => (
                  <MenuItem value={measurement.value}>{measurement.fullLabel} ({measurement.label})</MenuItem>
                ))}
              </Select>
          </FormControl>
        </Grid2>
      }
    </Grid2>
  </Stack>;
}