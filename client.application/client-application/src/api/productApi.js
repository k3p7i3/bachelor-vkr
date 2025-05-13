import { protocol, productServerUrl } from "../util/config"
import { authRequest } from "./httpRequest";

const url = `${protocol}://${productServerUrl}/api/product`;

const getProducts = ({productIds}) => {
  return authRequest({ method: 'post', url: url + '/products', body: productIds})
}

const getCart = ({userId}) => {
  var params = {
    userId: userId
  }
  return authRequest({ method: 'get', url: url + '/cart', params: params})
};

const addProductToCart = ({userId, product, count}) => {
  var body = {
    userId: userId,
    product: product,
    count: count
  }
  return authRequest({ method: 'post', url: url + '/cart/add', body: body})
};

const editCountForCartProduct = async ({cartProductId, count}) => {
  var body = {
    cartProductId: cartProductId,
    count: count
  }
  return authRequest({ method: 'post', url: url + '/cart/edit-count', body: body})
};

const deleteFromCart = async ({cartProductId}) => {
  var body = {
    id: cartProductId
  }
  return authRequest({ method: 'post', url: url + '/cart/remove', body: body});
}

export { getCart, addProductToCart, editCountForCartProduct, deleteFromCart,  getProducts };