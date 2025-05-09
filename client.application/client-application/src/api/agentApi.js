import { protocol, agentServerUrl } from "../util/config"
import {authRequest, notAuthRequest } from "./httpRequest";

const url = `${protocol}://${agentServerUrl}/api/agent`;

const registerAgent = ({agentProfile, userData}) => {
  var body = {
    agentProfile: agentProfile,
    userData: userData
  }

  return notAuthRequest({ method: 'post', url: url + '/create', body: body})
}

const editAgent = ({agentProfile}) => {
  return authRequest({ method: 'put', url: url + '/update', body: agentProfile})
}

const getAgent = ({agentId}) => {
  var params = {
      id: agentId
  }
  return notAuthRequest({ method: 'get', url: url, params: params})
}

const getAgentBrief = ({agentId}) => {
  var params = {
    id: agentId
  }
  return authRequest({ method: 'get', url: url + '/brief', params: params})
}

const getAgents = ({pageNumber, pageSize}) => {
  var params = {
    pageNumber: pageNumber,
    pageSize: pageSize
  }
  return notAuthRequest({ method: 'get', url: url + '/search', params: params})
}

const addAvatar = ({agentId, file}) => {
  let body = new FormData();
  body.append('agentId', agentId);
  body.append('file', file);

  return authRequest({ method: 'post', url: url + '/avatar', body: body});
}

const addImages = ({agentId, files}) => {
  let body = new FormData();
  body.append('agentId', agentId);
  body.append('files', files);
  return authRequest({ method: 'post', url: url + '/images', body: body});
}

const deleteImage = ({imageId}) => {
  var params = {
    imageId: imageId
  }
  return authRequest({ method: 'post', url: url + '/iamges', params: params});
}

const saveAgentSelection = ({userId, agentId}) => {
  var body = {
    userId: userId,
    agentId: agentId
  }
  return authRequest({ method: 'post', url: url + '/selection', body: body});
}

const getAgentSelection = ({userId}) => {
  var params = {
    userId: userId
  }
  return authRequest({ method: 'get', url: url + '/selection', params: params});
}

const deleteAgentSelection = ({userId}) => {
  var params = {
    userId: userId
  }
  return authRequest({ method: 'delete', url: url + '/selection', params: params});
}

export { 
  registerAgent, 
  editAgent, 
  getAgent, 
  getAgentBrief,
  getAgents,
  addAvatar,
  addImages,
  deleteImage,
  saveAgentSelection,
  getAgentSelection,
  deleteAgentSelection
};