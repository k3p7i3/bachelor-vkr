import { Typography, Stack, Grid2, Card, Chip, Box, Button } from "@mui/material";
import { useMemo } from "react";
import FeatureText from "../FeatureText";
import { useDispatch, useSelector } from "react-redux";
import { processPayment, selectConfirmationToken } from "../orderSlice";

function StaticPaymentCard({orderId, payment, tariffs, setPaymentWidgetShown}) {
  const confirmation_token = useSelector(selectConfirmationToken)
  const dispatch = useDispatch();
  const paymentStatus = useMemo(() => {
    if (payment.status === "succeeded") {
      return {text: "Оплачен", color: "success"}
    }
    if (payment.status === "canceled") {
      return {text: "Отменен", color: "error"}
    }
    return {text: "Ожидает оплаты", color: "primary"}
  }, [payment.status])

  const startPaymentProcess = () => {
    if (!confirmation_token) {
      dispatch(processPayment({orderId: orderId, paymentId: payment.id}));
    }
    setPaymentWidgetShown(true);
  }

  return <Card sx={{ p: 2 }}>
    <Grid2 container direction="row" spacing={3}>
      <Grid2 size={{xs: 12, sm: 6, md: 8}}>
        <Stack spacing={1}>
          {payment.description && <Typography>{payment.description}</Typography>}

          {payment.includedTariffs && payment.includedTariffs.length > 0 &&
            <>
              <Typography fontWeight="bold">Услуги, включенные в счет: </Typography>
              {payment.includedTariffs.map((tariff) => {
                var tariffInfo = tariffs.find((t) => t.tariffId === tariff.tariffId)
                return <Stack direction='row' spacing={1} sx={{ pl: 1}}>
                  <Typography>{tariffInfo.title}: </Typography>
                  <FeatureText feature={tariff.amount} fieldNames={['value']} numericType='PRICE'/>
                </Stack>
              })}
            </>
          }
          <Box display="flex" gap={1}>
            <Typography fontWeight="bold">Общая стоимость чека: </Typography>
            <FeatureText feature={payment.amount} fieldNames={['value']} numericType='PRICE'/>
          </Box>
        </Stack>
      </Grid2>

      <Grid2 container
        direction={{xs: "row", sm: "column"}}
        spacing={2}
        size={{xs: 12, sm: 6, md: 4}} 
        alignItems={"flex-end"}
        justifyContent={"space-evenly"}
      >
        <Chip label={paymentStatus.text} color={paymentStatus.color} variant="outlined"/>
        {paymentStatus.text === "Ожидает оплаты" &&
          <Button variant="contained" color="success" onClick={startPaymentProcess}>Оплатить</Button>
        }

      </Grid2>
    </Grid2>

  </Card>
};

export default StaticPaymentCard;