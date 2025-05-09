import { Grid2, Stack, Box, Typography } from "@mui/material";


function ProductBriefInfo({product, number}) {

  return <Grid2 container direction="row" spacing={1}>
    <Grid2 container alignItems="center">
      <Box>
        <img src={product.imageUrl} className="product_edit_image"/>
      </Box>
    </Grid2>  

    <Grid2 container size="grow">
      <Stack sx={{justifyContent: 'space-between'}}>
        <Typography sx={{ wordBreak: "break-all" }}>{product.title.slice(0, 45)}</Typography>
        <Typography>
          {product.skuParameters.join(', ')}
        </Typography>
        <Typography>
          Кол-во: {number}
        </Typography> 
      </Stack>
    </Grid2>
  </Grid2>;
}

export default ProductBriefInfo;