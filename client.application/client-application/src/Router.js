import { BrowserRouter } from "react-router-dom";
import { useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import { refreshToken } from "./api/authApi";
import { selectAuthData, setAuthData } from "./feature/authSlice";
import UnauthorizedApp from "./app/UnauthorizedApp";
import ClientApp from "./app/ClientApp";
import AgentApp from "./app/AgentApp";

export function Router() {
  const authData = useSelector(selectAuthData);
  const dispatch = useDispatch();

  useEffect(() => {
      var getNewToken = async (refresh_token) => {
          return await refreshToken(refresh_token);
      }

      if (!authData) {
          var refresh_token = localStorage.getItem('srt');
          if (refresh_token) {
            getNewToken(refresh_token)
              .then(response => {
                  dispatch(setAuthData(response.data));
              })
              .catch(error => {
                  localStorage.removeItem('srt')
              });
          }
      };
  }, [authData, dispatch]);

  return (
    <BrowserRouter>
      {!authData && <UnauthorizedApp/>}
      {authData && authData.role == "CLIENT" && <ClientApp/>}
      {authData && authData.role.startsWith("AGENT") && <AgentApp/>}
    </BrowserRouter>
  )
}