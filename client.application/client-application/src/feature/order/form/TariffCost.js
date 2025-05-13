import { Typography, Grid2, Paper } from "@mui/material";
import { mapUnitMeasurement } from "../../tariff/TariffMapUtils"
import { getTariffPriceCellText } from "../../tariff/TariffPriceCell";

export default function TariffCost({cost}) {
  const textSx = {
    color: 'var(--mui-palette-primary-main)',
    fontSize: '1.3rem',
    textAlign: 'center'
  }

  return <Paper variant="outlined" 
    sx={{
      borderColor: 'var(--mui-palette-primary-light)', 

      borderRadius: 3,
      p: 1.2, 
      width: 'fit-content' 
    }}>
    <Grid2 container direction="row" spacing={3}>
      <Grid2>
        <Typography sx={textSx}>{getTariffPriceCellText({price: cost.pricePerUnit})}</Typography>
        <Typography variant="caption" display={{ xs: 'none', md: 'block' }}>Применяемая цена</Typography>
      </Grid2>

      <Grid2><Typography>*</Typography></Grid2>

      <Grid2>
        <Typography sx={textSx}>{cost.unitAmount}</Typography>
        <Typography variant="caption" display={{ xs: 'none', md: 'block' }}>Множитель цены</Typography>
      </Grid2>

      <Grid2>
        <Typography>=</Typography>
      </Grid2>

      <Grid2>
        <Typography sx={textSx}>{cost.cost.value.toFixed(2) + mapUnitMeasurement[cost.cost.currency]}</Typography>
        <Typography variant="caption" display={{ xs: 'none', md: 'block' }}>Стоимость</Typography>
      </Grid2>

      <Grid2>
        <Typography>=</Typography>
      </Grid2>

      <Grid2>
        <Typography sx={textSx}>{cost.resultCost.value.toFixed(2) + mapUnitMeasurement[cost.resultCost.currency]}</Typography>
        <Typography variant="caption" display={{ xs: 'none', md: 'block' }}>К оплате</Typography>
      </Grid2>
  
    </Grid2>
  </Paper>
}