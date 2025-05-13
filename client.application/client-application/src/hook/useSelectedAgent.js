import { useSelector, useDispatch } from "react-redux";
import { useEffect } from "react";
import { selectAgentSelection, fetchSelectedBriefAgent } from "../feature/agent/agentSlice";
import { selectAuthUser } from "../feature/user/userSlice";

function useSelectedAgent() {
  const userData = useSelector(selectAuthUser);
  const selectedAgent = useSelector(selectAgentSelection);
  const dispatch = useDispatch();

  useEffect(() => {
    if (userData && selectedAgent === undefined) {
        dispatch(fetchSelectedBriefAgent({userId: userData.id}));
    }
  }, [userData, selectedAgent, dispatch]);

  return selectedAgent;
}

export default useSelectedAgent;