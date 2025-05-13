import { protocol, userServerUrl } from "../util/config"
import { authRequest, notAuthRequest } from "./httpRequest";

const url = `${protocol}://${userServerUrl}/api/user`;

const getUser = ({email: email}) => {
  var params = {
      email: email
  }
  return authRequest({ method: 'get', url: url, params: params })
}

const postUser = ({firstName, lastName, email, password}) => {
  var body = {
    firstName: firstName,
    lastName: lastName,
    email: email,
    password: password,
    role: "CLIENT"
  }
  return notAuthRequest({ method: 'post', url: url + '/register', body: body});
}

const editUser = ({username, firstName, lastName, email}) => {
  var body = {
    username: username,
    firstName: firstName,
    lastName: lastName,
    email: email
  }

  return authRequest({ method: 'post', url: url + '/update', body: body});
}

const editPassword = ({email, oldPassword, newPassword}) => {
  var body = {
    username: email,
    oldPassword: oldPassword,
    newPassword: newPassword
  }

  return authRequest({ method: 'post', url: url + '/change-password', body: body})
}

export { getUser, postUser, editUser, editPassword };