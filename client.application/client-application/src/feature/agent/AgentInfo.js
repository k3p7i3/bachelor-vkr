import { Typography, Box, Grid2, Button, ImageList, Stack, Dialog, Fab } from "@mui/material";
import { useDispatch } from "react-redux";
import { postAgentSelection, removeAgentSelection } from "./agentSlice";
import useAuthUser from "../../hook/useAuthUser";
import useSelectedAgent from "../../hook/useSelectedAgent";
import PhotoCameraIcon from '@mui/icons-material/PhotoCamera';
import CenteredMediumCard from "../../component/ui/CenteredMediumCard";
import CustomCarousel from "../../component/ui/CustomCarousel";
import AgentEditProfile from "./AgentEditProfile";

const AgentOrderButtons = ({agentId}) => {
  const dispatch = useDispatch();
  const user = useAuthUser();
  const agentSelection = useSelectedAgent();
  console.log(agentSelection, agentId);
  const isThisSelected = agentSelection?.agentId === agentId;

  const selectAgent = () => {
    if (isThisSelected) {
      if (user.id) {
        dispatch(removeAgentSelection({
          userId: user.id
        }))
      }
    } else {
      if (user.id && agentId) {
        dispatch(postAgentSelection({
            userId: user.id,
            agentId: agentId
        }))
      }
    }
  }

  return <>
    <Button 
      color={isThisSelected ? "secondary" : "primary"} 
      variant="contained"
      size="large"
      onClick={selectAgent}
    >
      {isThisSelected ? 'Удалить выбор для заказа' : 'Сохранить выбор для заказа'}
    </Button>
    <Button variant="contained" size="large">Оформить заказ</Button>
  </>
};

const addAvatarButton = () => {
  return <Fab size="small" sx={{ position: 'absolute', right: -5, bottom: -5 }}>
    <PhotoCameraIcon/>
  </Fab>
}

export default function AgentInfo({ agent, canEdit = false }) {
  const avatarUrl = (agent.avatar?.presignedUrl ||
    "https://pasrc.princeton.edu/sites/g/files/toruqf431/files/styles/3x4_750w_1000h/public/2021-03/blank-profile-picture_0.jpg?itok=YcR6ckN3");

  return <CenteredMediumCard>
    <Grid2 container direction="row" spacing={4}>
      <Grid2 size={3}>
        <Box sx={{ position: 'relative' }}>
          <img src={avatarUrl} className="agent-profile-avatar"/>
          {canEdit && addAvatarButton()}
        </Box>
      </Grid2> 

      <Grid2 container size={9} direction="column" sx={{justifyContent: 'space-between'}}>
        <Typography variant="h4">{agent.profile.name}</Typography>

        <Box>
          {agent.profile.contactPhoneNumber &&
            <Typography variant="subtitle1" color="textSecondary">Телефон: {agent.profile.contactPhoneNumber}</Typography>}

          {agent.profile.contactEmail &&
            <Typography variant="subtitle1" color="textSecondary">E-mail: {agent.profile.contactEmail}</Typography>}
        </Box>

        <Box sx={{display: 'flex', gap: '25px'}}>
          {canEdit 
            ? <AgentEditProfile agent={agent}/> 
            : <AgentOrderButtons agentId={agent.profile.id}/>
          }
        </Box>
      </Grid2>
    </Grid2>

    {agent.profile.description &&
      <Typography variant="body1">{agent.profile.description}</Typography>}


    {agent.images && agent.images.length > 0 &&
      <Stack spacing={2} >
        <Typography variant="h5">Фотографии</Typography>
  
        <CustomCarousel items={{large: 6, medium: 4, small: 3}} slidesToSlide={2}>
          {agent.images.map((image) => {
            return <img src={image.presignedUrl} className="mini-carousel-img"/>
          })}
        </CustomCarousel>
      </Stack>
    }
  </CenteredMediumCard>
};