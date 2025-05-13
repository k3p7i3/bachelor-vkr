import { useSearchParams, useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { useEffect } from "react";
import { protocol, authServerUrl, authClientId, clientUrl } from "../../util/config";
import { getToken } from "../../api/authApi";
import { setAuthData } from "../authSlice";
import generateCodes from "../../util/pkce";

const redirectToAuthLogin = () => {
  var codeChallenge = sessionStorage.getItem('codeChallenge');
  var link = `${protocol}://${authServerUrl}/oauth2/authorize?` +
    `response_type=code&` + 
    `client_id=${authClientId}&` + 
    `scope=read&` + 
    `redirect_uri=${protocol}://${clientUrl}/login&` + 
    `code_challenge=${codeChallenge}&` +
    `code_challenge_method=S256`;
  window.location.href = link;
};

const redirectToHomeScreen = async (code, navigate, dispatch) => {  
  const codeVerifier = sessionStorage.getItem('codeVerifier');
  const redirectUri = `${protocol}://${clientUrl}/login`
  await getToken(code, redirectUri, codeVerifier)
    .then((response) => {
        if (response.data.access_token) {
            localStorage.setItem('srt', response.data.refresh_token);
            dispatch(setAuthData(response.data));
            navigate('/');
        }
    })
    .catch(error => {
      navigate('/');
    })
};

function UserLogin() {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const dispatch = useDispatch();

  useEffect(() => {
    const code = searchParams.get('code');
    if (!code) {
        generateCodes();
        redirectToAuthLogin();
    } else {
        redirectToHomeScreen(code, navigate, dispatch);
    }
  }, [searchParams, navigate, dispatch])

  return (
      <span>Redirect...</span>
  )
}

export default UserLogin;