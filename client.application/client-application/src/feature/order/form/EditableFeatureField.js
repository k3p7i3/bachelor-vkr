import { Stack, Typography, MenuItem, IconButton } from "@mui/material";
import EditIcon from '@mui/icons-material/Edit';
import { useState } from "react";

import { mapUnitMeasurement } from "../../tariff/TariffMapUtils";
import { numericConditionTypeToMeasurements } from "../../tariff/form/tariffTableFormMapUtils";
import DenseTextField from "../../../component/ui/DenseTextField";
import DenseSelectField from "../../../component/ui/DenseSelectField";

function EditableFeatureField({fieldNames, numericType, feature, setFeature}) {
  const [isEditActive, setIsEditActive] = useState(false);

  const floatRegex = /^\d*\.?\d*$/;
  const [numericValues, setNumericValues] = useState(
    fieldNames.map((name) => (feature?.[name] ?? ''))
  );
  const [isValueValid, setIsValueValid] = useState(Array(fieldNames.length).fill(true));
  const [selectedUnit, setSelectedUnit] = useState(
    (numericType == "PRICE" ? feature?.currency : feature?.unit) ?? ''
  )

  const handleNumericValueChange = (e, fieldIndex) => {
    const inputValue = e.target.value;
    const isInputValid = floatRegex.test(inputValue);

    const otherValuesValid = 
      Array(fieldNames.length).every((_, idx) => (
        idx != fieldIndex ? (numericValues[idx] && isValueValid[idx]) : true
      ));

    setIsValueValid(
      isValueValid.map((v, idx) => (fieldIndex == idx) ? isInputValid : v)
    );
    setNumericValues(
      numericValues.map((v, idx) => (fieldIndex == idx) ? inputValue : v)
    );

    if (inputValue && isInputValid && otherValuesValid && selectedUnit) {
      const featureObj = {};
      fieldNames.forEach((name, idx) => {
        if (idx != fieldIndex) {
          featureObj[name] = parseFloat(numericValues[idx]);
        }
      });

      setFeature({
        ...featureObj,
        [fieldNames[fieldIndex]]: parseFloat(inputValue),
        [numericType == "PRICE" ? "currency" : "unit"]: selectedUnit
      });
    } else {
      setFeature(undefined);
    }
  };

  const handleUnitChange = (e) => {
    const unit = e.target.value;
    setSelectedUnit(unit);

    const numericValuesValid = (isValueValid.every((v) => (v)) && 
      numericValues.every((v) => (v))
    );

    if (numericValuesValid) {
      const featureObj = {};
      fieldNames.forEach((name, idx) => {
        featureObj[name] = parseFloat(numericValues[idx]);
      });

      setFeature({ ...featureObj, [numericType == "PRICE" ? "currency" : "unit"]: unit });
    } else {
      setFeature(undefined);
    }
  }

  const changeIsActiveOnEnter = (e) => {
    if (e.key === 'Enter') {
      setIsEditActive(!isEditActive);
    };
  };

  if (isEditActive) {
    return <Stack 
      direction="row"
    >
      {fieldNames.map((fieldName, idx) => (
        <DenseTextField
          key={fieldName}
          value={numericValues[idx]}
          onChange={(e) => handleNumericValueChange(e, idx)}
          error={!isValueValid[idx]}
          size="small"
          onKeyPress={changeIsActiveOnEnter}
        />
      ))}

      <DenseSelectField
        value={selectedUnit}
        onChange={handleUnitChange}
        size="small"
      >
        {numericConditionTypeToMeasurements[numericType]
          .map((measurement) => (
            <MenuItem value={measurement.value}>{measurement.label}</MenuItem>
          ))
        }
      </DenseSelectField>
    </Stack>;
  } else {
    return <Stack direction="row" sx={{ alignItems: "center"}}>
      <Typography>
        {feature 
          ? fieldNames.map((name) => feature[name]).join("x") + " " + 
            mapUnitMeasurement[feature[numericType == "PRICE" ? "currency" : "unit"]] 
          : "неизвестен"
        }
      </Typography>
      <IconButton onClick={(_) => setIsEditActive(!isEditActive)}>
        <EditIcon fontSize="small"/>
      </IconButton>
    </Stack>
  };
};

export default EditableFeatureField;