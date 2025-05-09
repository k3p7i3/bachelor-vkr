import * as crypto from 'crypto-js';
import sha256 from 'crypto-js/sha256';
import Base64 from 'crypto-js/enc-base64';

export let codeVerifier;
export let codeChallenge;

const base64Url = (str) => {
    return str.toString(Base64).replace(/=/g, '').replace(/\+/g, '-').replace(/\//g, '_');
}

const generateCodeVerifier = () => {
    return base64Url(crypto.enc.Base64.stringify(crypto.lib.WordArray.random(32)));
}

const generateCodeChallenge = (codeVerifier) => {
    return base64Url(sha256(codeVerifier));
}

const generateCodes = () => {
    codeVerifier = generateCodeVerifier();
    codeChallenge = generateCodeChallenge(codeVerifier);
    sessionStorage.setItem('codeVerifier', codeVerifier);
    sessionStorage.setItem('codeChallenge', codeChallenge);
}

export default generateCodes;