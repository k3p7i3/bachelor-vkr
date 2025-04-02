import { mapNumericType, mapUnitMeasurement } from "./TariffMapUtils"
import { Typography, Paper } from "@mui/material"

function TypeConditionTableCell({tariff, condition}) {
  const feature = tariff.features.find((f) => {
    return f.id == condition.featureId
  })

  const option = feature.options.find((op) => {
    return op.id == condition.optionId
  })

  return <Typography variant="body1">{option.title}</Typography>
}

export function NumericConditionTableCell({condition}) {
  let limits = null
  if (!condition.minLimit) {
    limits = `до ${condition.maxLimit}`
  }
  if (!condition.maxLimit) {
    limits = `от ${condition.minLimit}`
  }
  if (condition.minLimit && condition.maxLimit) {
    limits = `${condition.minLimit} — ${condition.maxLimit}`
  }

  return <Typography variant="body1">{limits} {mapUnitMeasurement[condition.measurementUnit]}</Typography>
}

export function ConditionTableCell({tariff, condition}) {
  let conditionCell = null

  if (condition.type == 'ENUM') {
    conditionCell = <TypeConditionTableCell tariff={tariff} condition={condition.typeCondition}/>
  }

  if (condition.type == 'NUMERIC') {
    conditionCell = <NumericConditionTableCell condition={condition.numericCondition}/>
  }

  return conditionCell;
}

export function ConditionHeaderTableCell({tariff, condition}) {
  let conditionHeader = null

  if (condition.type == 'ENUM') {
    const feature = tariff.features.find((f) => {
      return f.id == condition.typeCondition.featureId
    })
    conditionHeader = <Typography variant="body1">{feature.title}</Typography>
  }

  if (condition.type == 'NUMERIC') {
    conditionHeader = <Typography variant="body1">
      {mapNumericType[condition.numericCondition.measurementType]}
    </Typography>
  }

  return conditionHeader
}