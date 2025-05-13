import { Box, Button, Stack } from "@mui/material";
import { useNavigate } from "react-router-dom";
import NavBarBase from "./NavBarBase";
import AgentSearchBar from "./AgentSearchBar";

export default function UnauthorizedNavBar() {
  const navigate = useNavigate();

  return <NavBarBase barContent={
    <>
      <AgentSearchBar/>
     
      <Stack direction="row" >
        <Button color="inherit" disableRipple onClick={() => navigate('/client/registration')}>
          Вход для клиентов
        </Button>
        <Button color="inherit" disableRipple onClick={() => navigate('/agent/registration')}>
          Вход для посредников
        </Button>
      </Stack>
    </>
  }/>;
}