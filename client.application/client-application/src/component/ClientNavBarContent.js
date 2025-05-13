import { Avatar, Stack, Menu, MenuItem, IconButton } from "@mui/material";
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import { useSelector, useDispatch } from "react-redux";
import { useState } from "react";
import useAuthUser from "../hook/useAuthUser";
import { useNavigate } from "react-router-dom";
import AgentSearchBar from "./AgentSearchBar";
import NavBarBase from "./NavBarBase";
import { accessToken, setAccessToken } from "../api/httpRequest";
import { resetAuthData } from "../feature/authSlice";
import { resetAuth } from "../feature/user/userSlice";
import { logout as logoutApi } from "../api/authApi";

export default function ClientNavBar() {
  const user = useAuthUser();
  const dispatch = useDispatch();
  const navigate = useNavigate()

  const [anchorEl, setAnchorEl] = useState(null);
  const open = Boolean(anchorEl);
  const handleClick = (event) => {
    setAnchorEl(event.currentTarget);
  };
  const handleClose = () => {
    setAnchorEl(null);
  };

  const logout = async () => {
    localStorage.removeItem('srt');

    logoutApi(accessToken)
      .then(() => {
        localStorage.clear();
        sessionStorage.clear();
      })
      .finally(() => {
        setAccessToken(undefined);
        dispatch(resetAuthData());
        dispatch(resetAuth());
        navigate("/")
      });

  };

  const defaultAvatarUrl = "https://pasrc.princeton.edu/sites/g/files/toruqf431/files/styles/3x4_750w_1000h/public/2021-03/blank-profile-picture_0.jpg?itok=YcR6ckN3";

  return <NavBarBase barContent={
    <>
      <AgentSearchBar/>
        {user &&
          <Stack direction="row" spacing={3}>
            <IconButton href="/cart" sx={{color:"var(--mui-palette-common-white)"}}>
              <ShoppingCartIcon/>
            </IconButton>
            <Avatar alt="N" src={user?.imageUrl || defaultAvatarUrl} onClick={handleClick}/>
            <Menu
              id="client-nav-menu"
              anchorEl={anchorEl}
              open={open}
              onClose={handleClose}
            >
              <MenuItem onClick={() => navigate('/profile')}>Личный кабинет</MenuItem>
              <MenuItem onClick={() => navigate('/orders')}>Мои заказы</MenuItem>
              <MenuItem onClick={() => navigate('/cart')}>Корзина</MenuItem>
              <MenuItem onClick={logout}>Выйти</MenuItem>
            </Menu>
          </Stack>
        }
    </>
  }/>;
}