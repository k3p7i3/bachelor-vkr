import { useSelector, useDispatch } from "react-redux";
import { useEffect } from "react";
import { fetchAuthAgent, selectAuthAgent } from "../feature/agent/agentSlice";
import { selectAuthData } from "../feature/authSlice";
import { fetchAgentTariffs } from "../feature/tariff/tariffSlice";
import { selectAuthAgentInfo } from "../feature/agent/agentSelectors";

function useAuthAgent() {
  const dispatch = useDispatch();
  const auth = useSelector(selectAuthData);
  const agent = useSelector(selectAuthAgentInfo);

  useEffect(() => {
    console.log(agent);
    if (auth.agentId && !agent) {
      dispatch(fetchAuthAgent({ agentId: auth.agentId }));
      dispatch(fetchAgentTariffs({ agentId: auth.agentId}));
    } 
  }, [dispatch, auth, agent]);
  
  return agent;
};

export default useAuthAgent;