import { Stack, Card } from "@mui/material"
import ProductBriefInfo from "../product/ProductBriefInfo"

export default function OrderProductCardBase({product, orderProduct, featuresCard}) {
  return <Card
    key={orderProduct.productId}
    sx={{p: 2}}
  >
    <Stack spacing={1}>
      <ProductBriefInfo product={product} number={orderProduct.totalNumber}/>
      {featuresCard}
    </Stack>
  </Card>
};