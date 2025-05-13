import { getActiveValidTariffs } from "./orderSlice"

const mapAppliedTariff = (appliedTariff) => {
  return {
    tariffId: appliedTariff.tariffId,
    selectedCustomTypeOptions: Object.entries(appliedTariff.selectedOptions)
      .map(([featureId, optionId]) => (
        {featureId: featureId, optionId: optionId}
      )),
    finalCurrency: "RUB"
  }
}

const mapToOrderServiceCalcCostRequest = ({order}) => {
  const reqObj = {};

  const tariffId = order.calcCostForTariffTasks[0];
  const appliedTariff = order.appliedTariffs[tariffId];

  const appliedTariffReq = mapAppliedTariff(appliedTariff)

  let products = []
  if (appliedTariff.applyLevel === "ORDER") {
    products = Object.values(order.orderProducts)
  } else {
    products = appliedTariff.selectedProducts.map((productId) => (
      order.orderProducts[productId]
    ))
  };

  reqObj['appliedTariffs'] = [appliedTariffReq];
  reqObj['products'] = products;
  return {order: reqObj}
}

const mapToOrderCalcCostRequest = ({order}) => {
  const reqObj = {};
  const appliedTariffs = getActiveValidTariffs(order.appliedTariffs)

  const commonTariffs = appliedTariffs.filter((t) => (
    t.applyLevel === "ORDER" || t.selectedProducts.length === Object.keys(order.orderProducts).length
  ));
  const commonTariffsIds = commonTariffs.map((t) => t.tariffId);
  const localTariffs = appliedTariffs.filter((t) => (!commonTariffsIds.includes(t.tariffId)))

  const products = Object.values(order.orderProducts).map((orderProduct) => (
    {
      ...orderProduct,
      appliedTariffs: (localTariffs
        .filter((t) => (t.selectedProducts.includes(orderProduct.productId)))
        .map((t) => mapAppliedTariff(t))
      )
    }
  ))

  const appliedTariffsReq = commonTariffs.map((t) => mapAppliedTariff(t))

  reqObj['products'] = products
  reqObj['appliedTariffs'] = appliedTariffsReq
  return {order: reqObj}
}

const inputMapOrder = ({order})  => {
  const products = Object.values(order.orderProducts)
  const orderTariffs = getActiveValidTariffs(order.appliedTariffs).map((tariff) => (
    {
      tariffId: tariff.tariffId,
      applyLevel: tariff.applyLevel,
      includedOrderProductIds: tariff.selectedProducts,
      isAppliedToWholeOrder: tariff.applyLevel === "ORDER" || tariff.selectedProducts.length === products.length,
      selectedOptions: Object.entries(tariff.selectedOptions).map(([featureId, optionId]) => (
        {featureId: featureId, optionId: optionId}
      )),
      cost: tariff.cost,
      isFixed: tariff.isFixed,
      paymentStatus: tariff.paymentStatus,
      paidAmount: tariff.paidAmount
    }
  ))

  const result = {
    ...order,
    appliedTariffs: orderTariffs,
    products: products,
    clientId: order.userId
  }

  delete result.calcCostForTariffTasks
  delete result.orderProducts
  delete result.userId

  return {order: result}
}

const outputMapOrder = (order) => {
  const orderProducts = order.products.reduce((map, product) => {
    map[product.productId] = product;
    return map;
  }, {});

  const orderTariffs = order.appliedTariffs.reduce((map, tariff) => {
    map[tariff.tariffId] = {
      tariffId: tariff.tariffId,
      applyLevel: tariff.applyLevel,
      selectedProducts: tariff.includedOrderProductIds,
      selectedOptions: tariff.selectedOptions.reduce((optionMap, option) => {
        optionMap[option.featureId] = option.optionId;
        return optionMap
      }, {}),
      isDisabled: false,
      featuresCount: tariff.selectedOptions.length,
      cost: tariff.cost,
      isFixed: tariff.isFixed,
      paymentStatus: tariff.paymentStatus,
      paidAmount: tariff.paidAmount
    }
    return map;
  }, {});

  const result = {
    ...order,
    appliedTariffs: orderTariffs,
    orderProducts: orderProducts,
    userId: order.clientId,
    calcCostForTariffTasks: []
  }

  delete result.products
  delete result.clientId

  return result
}

export {
  mapToOrderServiceCalcCostRequest,
  mapToOrderCalcCostRequest,
  inputMapOrder,
  outputMapOrder
};