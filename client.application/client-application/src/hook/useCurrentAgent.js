import { useDispatch, useSelector } from "react-redux";
import { useParams } from "react-router-dom";
import { useRef, useEffect } from "react";
import { fetchCurrentAgent } from "../feature/agent/agentSlice";
import { selectCurrentAgentInfo } from "../feature/agent/agentSelectors";
import { fetchAgentTariffs } from "../feature/tariff/tariffSlice";

function useCurrentAgent() {
  const dispatch = useDispatch();
  const agent =  useSelector(selectCurrentAgentInfo);
  const params = useParams();
  const agentId = useRef();

  useEffect(() => {
    if (params.agentId != agentId.current) {
      dispatch(fetchCurrentAgent({ agentId: params.agentId }));
      dispatch(fetchAgentTariffs({ agentId: params.agentId }));
      agentId.current = params.agentId;
    }
  }, [dispatch, params, agent]);
  
  return agent;
};

export default useCurrentAgent;