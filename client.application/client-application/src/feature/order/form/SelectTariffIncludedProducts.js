import { Box, Typography, FormControlLabel, Checkbox, Grid2, Card, Stack } from "@mui/material";
import { useState } from "react";
import ProductBriefInfo from "../../product/ProductBriefInfo";
import { useDispatch } from "react-redux";
import { setAppliedTariff } from "../orderSlice";

function SelectTariffIncludedProducts({products, orderProducts, appliedTariff}) {
  const dispatch = useDispatch();
  const [selected, setSelected] = useState(appliedTariff.selectedProducts);

  const changeSelectForProduct = ({productId, checked}) => {
    const newSelectedArray = checked ? [...selected, productId] : selected.filter((v) => (v !== productId))
    changeSelected(newSelectedArray)
  }

  const changeSelected = (newSelectedArray) => {
    setSelected(newSelectedArray)
    dispatch(setAppliedTariff({
      ...appliedTariff,
      selectedProducts: newSelectedArray
    }));
  }

  return <Box>
    <Typography variant="subtitle1" fontWeight="bold">
      Выберите включенные продукты  
    </Typography>

    <FormControlLabel
      control={
        <Checkbox
          checked={selected.length === products.length}
          onChange={(e) => changeSelected(products.map((p) => p.id))}
          label="Включить все"
        />
      }
      label="Включить все"
    />

    <Grid2 container spacing={2}>
      {products.map((product) => (
        <Grid2 container key={product.id}>
          <Card 
            sx={{ 
              p:1, 
              backgroundColor: 'var(--mui-palette-grey-200)', 
              maxWidth: '25em',
              display: 'flex',
              justifyContent: 'center',
              height: '100%'
            }}
          > 
            <Stack direction="row" sx={{alignItems: "center"}}>
              <Checkbox 
                checked={selected.includes(product.id)}
                onChange={(e) => changeSelectForProduct({productId: product.id, checked: e.target.checked})}
              />
              <ProductBriefInfo product={product} number={orderProducts[product.id].totalNumber}/>
            </Stack>
          </Card>
        </Grid2>

      ))}
    </Grid2>
  </Box>;
};

export default SelectTariffIncludedProducts;