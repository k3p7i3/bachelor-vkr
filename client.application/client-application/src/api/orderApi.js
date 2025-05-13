import { protocol, orderServerUrl } from "../util/config"
import { authRequest } from "./httpRequest";

const url = `${protocol}://${orderServerUrl}/api/order`;

const getOrder = ({ orderId }) => {
  var params = {
    orderId: orderId
  }
  return authRequest({ method: 'get', url: url, params: params})
}

const postOrder = ({ order }) => {
  return authRequest({ method: 'post', url: url, body: order})
}

const updateOrder = ({ order }) => {
  return authRequest({ method: 'put', url: url, body: order})
}

const getClientOrders = ({ clientId }) => {
  var params = {
    clientId: clientId
  }
  return authRequest({ method: 'get', url: url + '/client', params: params})
}

const getAgentOrders = ({ agentId }) => {
  var params = {
    agentId: agentId
  }
  return authRequest({ method: 'get', url: url + '/agent', params: params})
}

const executePayment = ({ orderId, paymentId }) => {
  var body = {
    orderId: orderId,
    paymentId: paymentId
  }
  return authRequest({ method: 'post', url: url + '/payment', body: body})
}

export { getOrder, postOrder, updateOrder, getClientOrders, getAgentOrders, executePayment };