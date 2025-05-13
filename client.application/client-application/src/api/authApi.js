import qs from 'qs';
import axios from 'axios';
import { Buffer } from 'buffer';
import {protocol, authServerUrl, authClientId, authClientSecret} from '../util/config';

const url = `${protocol}://${authServerUrl}/oauth2`;

const getToken = (code, redirectUri, codeVerifier) => {
    var data = {
        client_id: authClientId,
        redirect_uri: redirectUri,
        grant_type: 'authorization_code',
        code: code,
        code_verifier: codeVerifier
    }
    return axios({
        url: url + '/token',
        method: 'post',
        headers: { 
          'Authorization': `Basic ${Buffer.from(authClientId + ':' + authClientSecret).toString('base64')}`,
          'Content-Type': 'application/x-www-form-urlencoded'
         },
        data: qs.stringify(data)
    })
}

const refreshToken = (refresh_token) => {
    var data = {
        'refresh_token': refresh_token,
        'grant_type': 'refresh_token',
    };

    return axios({
      url: url + '/token',
      method: 'post',
      headers: { 
        'Authorization': `Basic ${Buffer.from(authClientId + ':' + authClientSecret).toString('base64')}`,
        'Content-Type': 'application/x-www-form-urlencoded'
       },
      data: qs.stringify(data)
  })
}

const logout = async (access_token) => {
  return await axios({
    url: `${protocol}://${authServerUrl}/logout`,
    method: 'post',
    withCredentials: true,
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${access_token}`
    }
  });
}

export { getToken, refreshToken, logout };