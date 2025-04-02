import { Typography, Box, Grid2, Button, ImageList, Stack } from "@mui/material";
import CenteredMediumCard from "../../component/ui/CenteredMediumCard";
import CustomCarousel from "../../component/ui/CustomCarousel";

export default function AgentInfo({ agent }) {


  return <CenteredMediumCard>
    <Grid2 container direction="row" spacing={3}>
      <Grid2 size={3}>
        <img src={agent.avatarUrl} className="agent-profile-avatar"/>
      </Grid2> 

      <Grid2 container size={9} spacing={3} direction="column">
        <Typography variant="h4">{agent.name}</Typography>

        <Box>
          <Typography variant="subtitle1" color="textSecondary">Телефон: {agent.contactPhone}</Typography>
          <Typography variant="subtitle1" color="textSecondary">E-mail: {agent.contactEmail}</Typography>
        </Box>

        <Box>
          <Button variant="contained" size="large">Оформить заказ</Button>
        </Box>
      </Grid2>
    </Grid2>

    <Typography variant="body1">{agent.description}</Typography>

    <Stack spacing={2} >
      <Typography variant="h5">Фотографии</Typography>

      <CustomCarousel items={{large: 6, medium: 4, small: 3}} slidesToSlide={2}>
        {agent.images.map((image) => {
          return <img src={image} className="mini-carousel-img"/>
        })}
      </CustomCarousel>
    </Stack>

  </CenteredMediumCard>
};