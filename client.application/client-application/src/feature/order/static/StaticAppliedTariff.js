import { Stack, Grid2, Typography, Card } from "@mui/material";
import TariffCost from "../form/TariffCost";
import ProductBriefInfo from "../../product/ProductBriefInfo";
import AppliedTariffPaymentInfo from "../AppliedTariffPaymentInfo";

function StaticSelectTariffCategoryFeatures({feature, appliedTariff}) {
  const option = feature.options?.find((it) => it.id == appliedTariff.selectedOptions[feature.id]) 
  if (option) {
    return <Grid2 container spacing={1}>
      <Grid2 container size={6}> 
        <Typography variant="body1">{feature.title}:</Typography>
      </Grid2>
      <Grid2 container size={6}> 
        <Typography variant="body1" fontWeight="bold">{option.title}</Typography>
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
  } else {
    return <></>
  }
}

function StaticAppliedTariff({ tariff, appliedTariff, products, orderProducts }) {
  return <Card sx={{p: 2}}>
    <Stack spacing={2}>

      <Grid2 container direction="row" spacing={1} justifyContent="space-between" alignItems="center">      
        <Typography variant="h6">{tariff.title}</Typography>
        <AppliedTariffPaymentInfo appliedTariff={appliedTariff}/>
      </Grid2>

      <Grid2 container direction="row">
        <Grid2>
          {tariff.features?.map((feature) => <StaticSelectTariffCategoryFeatures key={feature.id} feature={feature} appliedTariff={appliedTariff}/>)}
        </Grid2>
      </Grid2>

      {(tariff.applyLevel == "PRODUCT") &&
          appliedTariff.selectedProducts.map((productId) => 
            <ProductBriefInfo product={products.find((it) => (it.id === productId))} number={orderProducts[productId].totalNumber}/>
          )
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
    </Stack>
  </Card>;
};

export default StaticAppliedTariff;