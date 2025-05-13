import { Button, Grid2, Stack, Typography, Dialog, Divider, Box, ButtonGroup } from "@mui/material";
import { useState } from "react";
import CenteredMediumCard from "../../component/ui/CenteredMediumCard";
import useAuthUser from "../../hook/useAuthUser";
import useSelectedAgent from "../../hook/useSelectedAgent";
import SelectedAgentCard from "../agent/SelectedAgentCard";
import EditProfileForm from "./EditUserProfileForm";
import EditPasswordForm from "./EditPasswordForm";



export default function UserProfile({ isClient }) {
  const user = useAuthUser();
  const selectedAgent = useSelectedAgent();
  const [openEditProfile, setOpenEditProfile] = useState(false);
  const [openEditPassword, setOpenEditPassword] = useState(false);

  const defaultAvatarUrl = "https://pasrc.princeton.edu/sites/g/files/toruqf431/files/styles/3x4_750w_1000h/public/2021-03/blank-profile-picture_0.jpg?itok=YcR6ckN3";

  return <CenteredMediumCard>
    {user &&
      <Grid2 container direction="row" spacing={3}>
        <Grid2 size={{ sm: 4, md: 3}} offset={{md: 1}} sx={{ display: 'flex', alignItems: 'center'}}>
          <div>
            <img src={user.imageUrl || defaultAvatarUrl} className="client-avatar"/>
          </div>
        </Grid2>

        <Grid2 size={7} offset={1} container>
          <Stack sx={{ alignItems: 'flex-start', justifyContent: 'center', width: '100%'}} spacing={2}>
            <Typography variant="h6">{user.firstName + " " + user.lastName}</Typography>
            <Typography variant="h6">E-mail: {user.email}</Typography>

            {isClient &&
              <Box>
                <Typography>Код для расширения:</Typography>
                <Typography color="success">{user.id}</Typography>
              </Box>
            }
            
            <ButtonGroup>
              <Button onClick={() => setOpenEditProfile(true)}>Редактировать профиль</Button>
              <Button onClick={() => setOpenEditPassword(true)}>Изменить пароль</Button>
            </ButtonGroup>


            <Dialog
              open={openEditProfile}
              onClose={() => setOpenEditProfile(false)}
            >
              <EditProfileForm user={user} setIsOpen={setOpenEditProfile}/>
            </Dialog>

            <Dialog
              open={openEditPassword}
              onClose={() => setOpenEditPassword(false)}
            >
              <EditPasswordForm email={user.email} setIsOpen={setOpenEditPassword}/>
            </Dialog>
          </Stack>
        </Grid2>

        <Grid2 size={12}>
          {isClient && <>
            <Divider sx={{mb: 2}}/>
            <SelectedAgentCard userId={user.id} selectedAgent={selectedAgent}/>
          </>}
        </Grid2>

      </Grid2>
    }
  </CenteredMediumCard>;
}