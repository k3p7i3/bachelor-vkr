import { useDispatch } from "react-redux";
import { useState } from "react";
import { Typography, Box, Card, Stack, Checkbox, Grid2, Switch, Paper, FormControlLabel } from "@mui/material";

import CenteredLargeCard from "../../../component/ui/CenteredLargeCard";
import SelectTariffIncludedProducts from "./SelectTariffIncludedProducts";
import { setAppliedTariff } from "../orderSlice";
import TariffCost from "./TariffCost";
import AppliedTariffPaymentInfo from "../AppliedTariffPaymentInfo";

function SelectTariffCategoryFeatures({ feature, appliedTariff }) {
  const dispatch = useDispatch();
  const [selectedOption, setSelectedOption] = useState(appliedTariff.selectedOptions[feature.id] || '');

  const handleSelect = (e, id) => {
    if (e.target.checked) {
      setSelectedOption(id);
      dispatch(setAppliedTariff({
        ...appliedTariff,
        selectedOptions: {
          ...appliedTariff.selectedOptions,
          [feature.id]: id
        }
      }))
    }
  }

  return <Stack spacing={1}>
    <Typography fontWeight="bold">{feature.title}</Typography>
    <Typography variant="caption" color="textSecondary">{feature.description}</Typography>

    {feature.options?.map((option) => (
        <Stack key={option.id} direction="row" spacing={1}>
          <Checkbox
            checked={selectedOption === option.id}
            onChange={(e) => handleSelect(e, option.id)}
          />
          <Box sx={{maxWidth: '20em',}}>
            <Typography>{option.title}</Typography>
            <Typography sx={{ wordBreak: "break-all" }} variant="caption" color="textSecondary">{option.description}</Typography>     
          </Box>
        </Stack>
    ))}
  </Stack>
}

function AppliedTariffOrderForm({ tariff, appliedTariff, products, orderProducts }) {
  return <Stack spacing={2}>
    <Grid2 container direction="row">
      <Grid2>
        {tariff.features?.map((feature) => <SelectTariffCategoryFeatures key={feature.id} feature={feature} appliedTariff={appliedTariff}/>)}
      </Grid2>
    </Grid2>


    {(tariff.applyLevel == "PRODUCT") &&
        <SelectTariffIncludedProducts products={products} orderProducts={orderProducts} appliedTariff={appliedTariff}/>
    }

    {appliedTariff.cost 
      ? <Stack spacing={1}>
        <Typography variant="subtitle1" fontWeight="bold">Предположительная стоимость услуги</Typography>
        <TariffCost cost={appliedTariff.cost}/>
      </Stack>
      : <Typography variant="subtitle1" fontWeight="bold" color="textSecondary">
        Не получилось рассчитать стоимость услуги: не хватает данных или нет подходящих условий тарифа  
        </Typography>
    }
  </Stack>;
}


function ApplyTarifOrderForm({ tariff, appliedTariff, products, orderProducts}) {
  const dispatch = useDispatch();
  const [isDisabled, setIsDisabled] = useState(appliedTariff.isDisabled);

  const setAppliedTariffIsDisabled = (e) => {
    setIsDisabled(e);
    if (e) {
      dispatch(setAppliedTariff({
        ...appliedTariff,
        isDisabled: true
      }))
    } else {
      dispatch(setAppliedTariff({
        ...appliedTariff,
        isDisabled: false
      }))
    }
  }

  return <CenteredLargeCard>
    <Stack direction="row" justifyContent='space-between'>
      <Stack direction="row" spacing={2}>
        <Switch checked={!isDisabled} onChange={(e) => setAppliedTariffIsDisabled(!e.target.checked)}/>
        <Typography variant="h6" color={isDisabled ? "textSecondary" : "textPrimary"}>{tariff.title}</Typography>
      </Stack>

      
      <AppliedTariffPaymentInfo appliedTariff={appliedTariff}/>
    </Stack>

    {!isDisabled && 
      <AppliedTariffOrderForm 
        tariff={tariff} appliedTariff={appliedTariff} 
        products={products} orderProducts={orderProducts}
      />
    }
  </CenteredLargeCard>;
};

export default ApplyTarifOrderForm;