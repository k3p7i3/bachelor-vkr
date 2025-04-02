import { Grid2, Box, Typography, Stack, Button, Divider } from "@mui/material";
import StarIcon from '@mui/icons-material/Star';
import { green, yellow, red } from '@mui/material/colors';
import CenteredMediumCard from "../../component/ui/CenteredMediumCard"
import CustomCarousel from "../../component/ui/CustomCarousel"
import { useEffect, useState } from "react";
import { TariffPriceCell } from "./tariff/TariffPriceCell";

function TariffMinPrice({ tariff }) {
  let minPrice = undefined;

  tariff.tariffTables.forEach(table => {
    table.tariffPrices.forEach(tableRow => {
      tableRow.forEach(pricePerUnit => {
        if (minPrice == undefined || minPrice.price.value < pricePerUnit.price.value) {
          minPrice = pricePerUnit;
        }
      })
    })
  });

  return <Grid2 container direction="row" alignItems='baseline' spacing={1}>  
    <Grid2 size="auto">
      <Typography variant="body1">{tariff.title}</Typography>
    </Grid2>
    <Grid2 size="grow" sx={{ borderBottom: '1px solid var(--mui-palette-divider)' }}/>
    <Grid2 container size="auto" direction='row'>
      <Typography>от </Typography>
      <TariffPriceCell price={minPrice}/>
    </Grid2>
  </Grid2>;
}
 
export default function AgentPreviewCard({ agent, tariffs }) {

  let starColor = undefined;
  if (agent.rating >= 4) {
    starColor = green[500];
  } else if (agent.rating >= 3) {
    starColor = yellow[500];
  } else {
    starColor = red[500];
  }  

  return <CenteredMediumCard>
    <Grid2 container direction="row" spacing={3}>

      <Grid2 container size={3} direction="column" spacing={1}>
        <img src={agent.avatarUrl} className="agent-profile-avatar"/>
        <Stack direction="row" sx={{ alignItems: 'center'}}>
          <StarIcon sx={{ color: starColor }} fontSize="large"/>
          <Typography variant="h5">{agent.rating}</Typography>
        </Stack>
        <Typography variant="body1">{agent.reviewsNumber} оценки</Typography>
      </Grid2> 

      <Grid2 container size={9} spacing={2} direction="column">
        <Typography variant="h5">{agent.name}</Typography>

        <Stack direction="row" spacing={3}>
         <Typography variant="subtitle1" color="textSecondary">Телефон: {agent.contactPhone}</Typography>
          <Typography variant="subtitle1" color="textSecondary">E-mail: {agent.contactEmail}</Typography>
        </Stack>

        <Typography variant="body1">
          {agent.description.slice(0, 190)}{agent.description.length > 190 ? '...' : ''}
        </Typography>

        <Divider/>

        <CustomCarousel items={{large: 5, medium: 4, small: 2}} slidesToSlide={2}>
          {agent.images.map((image) => {
            return <img src={image} className="mini-carousel-img"/>
          })}
        </CustomCarousel>

        <Divider/>
          {
            tariffs.map((tariff) => {
              return <TariffMinPrice tariff={tariff}/>;
            })
          }
        <Divider/>

        <Button variant="contained">Оформить заказ</Button>
      </Grid2>
    
    </Grid2>


  </CenteredMediumCard>
};