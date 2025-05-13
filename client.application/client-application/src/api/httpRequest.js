import axios from 'axios';

export let accessToken;

export const setAccessToken = (value) => {
  accessToken = value;
}

export function authRequest({ method, url, body, params, headers }) {
    let requesthHeaders = { 'Authorization': `Bearer ${accessToken}` }
    if (headers) {
      requesthHeaders = {
        ...headers,
        ...requesthHeaders
      }
    }

    const config = {
        url: url,
        method: method,
        headers: requesthHeaders,
    }

    if (body) {
        config.data = body;
    }

    if (params) {
        config.params = params;
    }

    return axios(config);
};

export function notAuthRequest({ method, url, body, params, headers }) {
  const config = {
    url: url,
    method: method
  }

  if (headers) {
    config.headers = headers;
  }

  if (body) {
    config.data = body;
  }

  if (params) {
    config.params = params;
  }

  return axios(config);
};