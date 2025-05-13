import { Grid2, Typography, Box } from "@mui/material";
import { amber } from '@mui/material/colors';
import { useMemo } from "react";
import { mapUnitMeasurement } from "../tariff/TariffMapUtils";


function AppliedTariffPaymentInfo({appliedTariff}) {
  let paidText = useMemo(() => {
    if (appliedTariff.paymentStatus === "PAID") {
      return "Оплачено";
    }
    const paidAmountIsPartial = appliedTariff.paidAmount && (!appliedTariff.cost?.resultCost
      || (appliedTariff.cost.resultCost && appliedTariff.paidAmount.value < appliedTariff.cost.resultCost.value))
    if (appliedTariff.paymentStatus === "PAID_PARTIALLY" || paidAmountIsPartial) {
      return (
        `Оплачено 
          ${appliedTariff?.paidAmount?.value || '0'}
            / ${appliedTariff?.cost?.resultCost?.value  || ''}
          ${mapUnitMeasurement[appliedTariff?.paidAmount?.currency] || ''}`
      )
    }
    return ''
  }, [appliedTariff.status, appliedTariff.paidAmount, appliedTariff.cost])


  if (paidText || appliedTariff.paymentStatus === "WAITING") {
    return <Grid2 container direction="row" spacing={2}>
      {paidText && <Typography color='textSecondary'>{paidText}</Typography>}
      {appliedTariff.paymentStatus === "WAITING" &&
        <Box sx={{ p: 1, backgroundColor: amber[100], width: 'fit-content', borderRadius: 2}}>
          <Typography > ⚠️ Есть неоплаченные счета</Typography>
        </Box>
      }
    </Grid2>
  } else {
    return <></>
  }
};

export default AppliedTariffPaymentInfo;