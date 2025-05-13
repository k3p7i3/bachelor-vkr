import { protocol, tariffServerUrl } from "../util/config"
import {authRequest, notAuthRequest } from "./httpRequest";

const tariffUrl = `${protocol}://${tariffServerUrl}/api/tariff`;
const currencyUrl = `${protocol}://${tariffServerUrl}/api/currency`;
const taxableUrl = `${protocol}://${tariffServerUrl}/api/taxable`;

const getAgentTariffs = ({agentId}) => {
  var params = {
      agentId: agentId
  }
  return notAuthRequest({ method: 'get', url: tariffUrl, params: params })
}

const getAgentsBriefTariffs = ({agentIds}) => {
  return notAuthRequest({ method: 'post', url: tariffUrl + '/brief', body: agentIds })
}

const getTariff = ({tariffId}) => {
  return notAuthRequest({ method: 'get', url: tariffUrl + '/' + tariffId })
}

const createTariff = ({tariff}) => {
  return authRequest({ method: 'post', url: tariffUrl, body: tariff })
}

const updateTariff = ({tariff}) => {
  return authRequest({ method: 'put', url: tariffUrl, body: tariff })
}

const deleteTariff = ({tariffId}) => {
  return authRequest({ method: 'delete', url: tariffUrl + '/' + tariffId })
}

const getCurrencyRates = ({agentId}) => {
  return notAuthRequest({ method: 'get', url: currencyUrl })
}

const saveCurrencyRates = ({agentId, rates}) => {
  var body = {
    agentId: agentId,
    exchangeRates: rates
  }
  return authRequest({ method: 'post', url: currencyUrl, body: body })
}

const calculateOrderCost = ({order}) => {
  return authRequest({ method: 'post', url: taxableUrl + '/calculate/order', body: order })
}

const calculateOrderServiceCost = ({order}) => {
  return authRequest({ method: 'post', url: taxableUrl + '/calculate/service', body: order })
}

export {
  getAgentsBriefTariffs,
  getAgentTariffs,
  getTariff,
  createTariff,
  updateTariff,
  deleteTariff,
  getCurrencyRates,
  saveCurrencyRates,
  calculateOrderCost,
  calculateOrderServiceCost
};