import { Typography, Card, Stack, FormControlLabel } from "@mui/material"
import Checkbox from "@mui/material/Checkbox"
import { useState } from "react"
import EditableFeatureField from "../form/EditableFeatureField"

function AddPaymentForm({tariffs}) {
  return <Card>
    <Typography>Выставить счет</Typography>
    {tariffs.map((tariff) => (
      <AddTariffToPaymentRow tariff={tariff}/>
    ))}
  </Card>
};

export default AddPaymentForm;

function AddTariffToPaymentRow({tariff}) {
  const [price, setPrice] = useState('');

  return <Stack direction="row">
    <FormControlLabel
      label={tariff.title}
      control={
        <Checkbox/>
      }
    />
    <EditableFeatureField 
      fieldNames={['value']}
      numericType='PRICE'
      feature={price}
      setFeature={setPrice}
    />
  </Stack>
}