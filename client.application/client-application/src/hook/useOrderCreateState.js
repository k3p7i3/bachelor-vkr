import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { fetchTariffsForOrder } from "../feature/tariff/tariffSlice";
import { fetchSelectedBriefAgent } from "../feature/agent/agentSlice";
import { selectCreateOrderInfo } from "../feature/order/orderSelectors";
import { initAppliedTariffs, calcOrderServiceCostForCreateForm, calcOrderCostForCreateForm } from "../feature/order/orderCreateSlice";

function useOrderCreateState() {
  const dispatch = useDispatch();
  const orderData = useSelector(selectCreateOrderInfo);

  useEffect(() => {
    console.log("use effect", orderData);

    const userId = orderData?.userId
    if (userId) {
      if (!orderData.selectedAgent) {
        dispatch(fetchSelectedBriefAgent({userId: userId}))
      } else {
        if (!orderData.tariffs) {
          const agentId = orderData.selectedAgent.agentId
          dispatch(fetchTariffsForOrder({agentId: agentId}))
        } else {
          if (!orderData.appliedTariffs) {
            dispatch(initAppliedTariffs({tariffs: orderData.tariffs}))
          } else {
            if (orderData.calcCostForTariffTasks.length > 0) {
              if (orderData.calcCostForTariffTasks.length === 1) {
                dispatch(calcOrderServiceCostForCreateForm({order: orderData}))
              } else {
                dispatch(calcOrderCostForCreateForm({order: orderData}))
              }
            }
          }
        }
      }
    }
  }, [dispatch, orderData]);
  
  return orderData;
};

export default useOrderCreateState;