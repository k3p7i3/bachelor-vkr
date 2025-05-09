import { Grid2, Box, Typography, Stack, Link, IconButton, Checkbox, Grid } from "@mui/material";
import CenteredMediumCard from "../../component/ui/CenteredMediumCard";
import { mapUnitMeasurement } from "../tariff/TariffMapUtils";
import { useState } from "react";
import Counter from "../../component/ui/Counter";
import DeleteIcon from '@mui/icons-material/Delete';
import { removeFromCart, editCount } from "./productSlice";
import { useDispatch } from "react-redux";


function ProductCardPrice({price}) {
  return <Box 
    sx={{ 
      borderRadius: 2, 
      p: 1.3,
      backgroundColor: 'var(--mui-palette-secondary-light)',
      width: 'fit-content',
    }}
  >
    <Typography variant="h6" sx={{ fontWeight: 'bold', color: "var(--mui-palette-secondary-contrastText)" }}>
      {price.value} {mapUnitMeasurement[price.currency]}
    </Typography>
  </Box>
};

function ProductCard({cartProduct, changeSelected}) {
  const dispatch = useDispatch();

  const [count, setCount] = useState(cartProduct.count || 1)

  const updateProductCount = (cnt) => {
    setCount(cnt);
    dispatch(editCount({cartProductId: cartProduct.id, count: cnt}));
  }

  return <CenteredMediumCard>
    <Grid2 container direction="row" spacing={2} sx={{alignItems: "center"}}>
        <Grid2 size={{xs: 3, sm: 1}}>
          <Checkbox onChange={(e) => changeSelected({productId: cartProduct.id, checked: e.target.checked})}/>
        </Grid2>

        <Grid2 size={{xs: 8, sm: 4, md: 4}}>
          <Box>
            <img src={cartProduct.product.imageUrl} className="product_cart_image"/>
          </Box>
        </Grid2>

        <Grid2 size={{xs: 12, sm: 7, md: 7}}>
          <Stack direction="column" spacing={2} sx={{flexGrow: 1}}>
              <Link 
                href={cartProduct.product.productUrl} 
                color="inherit"
                variant="h6" 
                underline="hover"
                target="_blank"
                rel="noopener"
                sx={{ wordBreak: "break-all" }}
              > 
                {cartProduct.product.title.slice(0, 50)}
              </Link>

            <Grid2 container direction="row" justifyContent="space-between" spacing={2}>
              <Grid2 size={{xs: 12, sm: 5, md: 6}}>
                  <Stack spacing={1}>
                    <Typography color="secondary" fontWeight="bold">{cartProduct.product.marketplace}</Typography>
                    
                    <Typography fontWeight="bold">
                      {cartProduct.product.skuParameters.join(', ')}
                    </Typography>

                    <Grid2 container direction="row" spacing={1} columns={2}>
                      <Grid2>
                        {cartProduct.product.weight &&
                          <Typography color="textSecondary">
                            Вес: {cartProduct.product.weight.value}{mapUnitMeasurement[cartProduct.product.weight.unit]}
                          </Typography>
                        }
                      </Grid2>

                      <Grid2>
                        {cartProduct.product.boxVolume &&
                          <Typography color="textSecondary">
                            Объем: {cartProduct.product.boxVolume.length}x{cartProduct.product.boxVolume.width}x{cartProduct.product.boxVolume.height}
                            {mapUnitMeasurement[cartProduct.product.boxVolume.unit]}
                          </Typography>
                        }
                      </Grid2>

                      <Grid2>
                        {cartProduct.product.volume &&
                          <Typography color="textSecondary">
                            Объем: {cartProduct.product.volume.value}{mapUnitMeasurement[cartProduct.product.volume.unit]}
                          </Typography>
                        }
                      </Grid2>
                    </Grid2>
                  </Stack>
              </Grid2>

              <Grid2 size={{xs: 12, sm: 7, md: 6}}>
                <Stack spacing={3} sx={{ alignItems: "flex-end" }}>
                  {cartProduct.product.price && <ProductCardPrice price={cartProduct.product.price}/>}

                  <Stack direction="row" spacing={1} sx={{ alignItems: "center" }}>
                    <IconButton 
                      onClick={() => dispatch(removeFromCart({cartProductId: cartProduct.id}))}
                    >
                      <DeleteIcon/>
                    </IconButton>
                    <Counter count={count} setCount={updateProductCount}/>
                  </Stack>
                </Stack>
              </Grid2>

            </Grid2>
          </Stack>
        </Grid2>

    </Grid2>
  </CenteredMediumCard>;
};

export default ProductCard;