import { Avatar, Stack, Menu, MenuItem } from "@mui/material";
import { useSelector, useDispatch } from "react-redux";
import { useState } from "react";
import useAuthUser from "../hook/useAuthUser";
import { useNavigate } from "react-router-dom"
import NavBarBase from "./NavBarBase";
import { accessToken, setAccessToken } from "../api/httpRequest";
import { resetAuthData } from "../feature/authSlice";
import { resetAuth } from "../feature/user/userSlice";
import { logout as logoutApi } from "../api/authApi";

export default function AgentNavBar() {
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
        {user &&
          <Stack direction="row">
            <Avatar alt="N" src={user?.imageUrl || defaultAvatarUrl} onClick={handleClick}/>
            <Menu
              id="client-nav-menu"
              anchorEl={anchorEl}
              open={open}
              onClose={handleClose}
            >
              <MenuItem onClick={() => navigate('/agent/profile')}>Профиль посредника</MenuItem>
              <MenuItem onClick={() => navigate('/orders')}>Заказы</MenuItem>
              <MenuItem onClick={() => navigate('/profile')}>Личный кабинет</MenuItem>
              <MenuItem onClick={logout}>Выйти</MenuItem>
            </Menu>
          </Stack>
        }
    </>
  }/>;
}