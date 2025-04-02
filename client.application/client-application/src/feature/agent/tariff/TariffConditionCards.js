import { mapNumericType, mapUnitMeasurement } from "./TariffMapUtils"
import { Grid2, Typography, Stack, Paper } from "@mui/material"

function TariffCommonTypeCondition({tariff, condition}) {
  const feature = tariff.features.find((f) => {
    return f.id == condition.featureId
  })

  const option = feature.options.find((op) => {
    return op.id == condition.optionId
  })
  
  return <Grid2 container columnSpacing={1}>
    <Grid2 container size={6}> 
      <Typography variant="body1">{feature.title}:</Typography>
    </Grid2>

    <Grid2 container size={6}> 
      <Typography variant="body1">{option.title}</Typography>
    </Grid2>

    <Grid2 container size={6}> 
      {feature.description && 
        <Typography sx={{lineHeight: '1.1'}} color="textSecondary" variant="caption">{feature.description}</Typography>}
    </Grid2>

    <Grid2 container size={6}> 
      {option.description && 
        <Typography sx={{lineHeight: '1.1'}} color="textSecondary" variant="caption">{option.description}</Typography>}
    </Grid2>
  </Grid2>
}

function TariffCommonNumericCondition({condition}) {

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

  return <Stack spacing={1}>
    <Typography variant="body1">{mapNumericType[condition.measurementType]}:</Typography>
    <Typography variant="body1">{limits} {mapUnitMeasurement[condition.measurementUnit]}</Typography>
  </Stack>
}

export function TariffConditionCard({tariff, condition}) {
  let conditionCard = null
  if (condition.type == 'ENUM') {
    conditionCard = <TariffCommonTypeCondition tariff={tariff} condition={condition.typeCondition}/>
  }

  if (condition.type == 'NUMERIC') {
    conditionCard = <TariffCommonNumericCondition condition={condition.numericCondition}/>
  }

  return <Paper 
    variant="outlined" 
    sx={{maxWidth: '350px', width: 'fit-content', padding: '10px'}}
  >
    {conditionCard}
  </Paper>
}

export default function TariffConditionCards({tariff, conditions}) {
  return <Grid2 container spacing={2} direction='row'>
    {conditions && 
      conditions.map((condition) => {
        return <TariffConditionCard tariff={tariff} condition={condition}/>
      })
    }
  </Grid2>
}
