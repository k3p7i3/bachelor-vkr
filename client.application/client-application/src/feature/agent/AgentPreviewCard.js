import { Grid2, Box, Typography, Stack, Button, Divider } from "@mui/material";
import StarIcon from '@mui/icons-material/Star';
import { green, yellow, red } from '@mui/material/colors';
import CenteredMediumCard from "../../component/ui/CenteredMediumCard"
import CustomCarousel from "../../component/ui/CustomCarousel"
import { useEffect, useState } from "react";
import { TariffPriceCell } from "../tariff/TariffPriceCell";
import { useNavigate } from "react-router-dom";

function TariffMinPrice({ tariff }) {
  return <Grid2 container direction="row" alignItems='baseline' spacing={1}>  
    <Grid2 size="auto">
      <Typography variant="body1">{tariff.title}</Typography>
    </Grid2>
    <Grid2 size="grow" sx={{ borderBottom: '1px solid var(--mui-palette-divider)' }}/>
    <Grid2 container size="auto" direction='row'>
      <Typography>от </Typography>
      <TariffPriceCell price={tariff.price}/>
    </Grid2>
  </Grid2>;
}
 
export default function AgentPreviewCard({ agent, tariffs }) {

  const navigate = useNavigate();
  const redirectToAgentProfile = () => {
    navigate(agent.profile.id);
  };

  let starColor = undefined;
  if (agent.rating.rating >= 4) {
    starColor = green[500];
  } else if (agent.rating.rating >= 3) {
    starColor = yellow[500];
  } else {
    starColor = red[500];
  }

  const avatarUrl = (agent.avatar?.presignedUrl ||
    "https://pasrc.princeton.edu/sites/g/files/toruqf431/files/styles/3x4_750w_1000h/public/2021-03/blank-profile-picture_0.jpg?itok=YcR6ckN3");

  return <CenteredMediumCard>
    <Grid2 container direction="row" spacing={3}>

      <Grid2 container size={3} direction="column" spacing={1}>
        <img src={avatarUrl} className="agent-profile-avatar"/>
        {agent.rating.reviewsNumber > 0 &&
          <Stack direction="row" sx={{ alignItems: 'center'}}>
            <StarIcon sx={{ color: starColor }} fontSize="large"/>
            <Typography variant="h5">{agent.rating.rating}</Typography>
          </Stack>
        }
        <Typography variant="body1">{agent.rating.reviewsNumber} оценок</Typography>
      </Grid2> 

      <Grid2 container size={9} spacing={2} direction="column">
        <Typography 
          onClick={redirectToAgentProfile} 
          sx={{ "&:hover": { color: 'var(--mui-palette-primary-dark)' } }}
          variant="h5"
        >
          {agent.profile.name}
        </Typography>

        <Stack direction="row" spacing={3}>
          {agent.profile.contactPhoneNumber &&
            <Typography variant="subtitle1" color="textSecondary">Телефон: {agent.profile.contactPhoneNumber}</Typography>}
          {agent.profile.contactEmail &&
            <Typography variant="subtitle1" color="textSecondary">E-mail: {agent.profile.contactEmail}</Typography>}
        </Stack>

        {agent.profile.description &&
          <Typography variant="body1">
            {agent.profile.description.slice(0, 190)}{agent.profile.description.length > 190 ? '...' : ''}
          </Typography>
        }



        {agent.images && agent.images.length != 0 &&
          <>
            <Divider/>
            <CustomCarousel items={{large: 5, medium: 4, small: 2}} slidesToSlide={2}>
              {agent.images.map((image) => {
                return <img src={image.presignedUrl} className="mini-carousel-img"/>
              })}
            </CustomCarousel>  
          </>
        }

        <Divider/>
          {
            tariffs.map((tariff) => {
              return <TariffMinPrice tariff={tariff}/>;
            })
          }

        <Button variant="contained">Оформить заказ</Button>
      </Grid2>
    </Grid2>


  </CenteredMediumCard>
};