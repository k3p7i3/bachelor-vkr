import { AppBar, Toolbar, Box, Typography, Button } from "@mui/material";

import BasicLayout from "../component/ui/BasicLayout";
import LoginForm from "../feature/user/UserLoginForm";
import NavBarBase from "../component/NavBarBase";
import UnauthorizedNavBarContent from "../component/UnauthorizedNavBarContent";

export default function LoginPage() {
  return (
    <BasicLayout
      header={
        <NavBarBase 
          barContent={<UnauthorizedNavBarContent/>}
        />      
      }
      content={<LoginForm/>}
    />
  )
};