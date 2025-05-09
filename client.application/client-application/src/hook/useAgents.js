import { useDispatch, useSelector } from "react-redux";
import { useRef } from "react";
import { useSearchParams } from "react-router-dom";
import { fetchAgents, selectAgents } from "../feature/agent/agentSlice";
import { useEffect } from "react";

function useAgents() {
  const dispatch = useDispatch();
  const agents =  useSelector(selectAgents);
  const [searchParams, setSearchParams] = useSearchParams();
  const pageNumber = useRef();

  useEffect(() => {
    let paramsPageNumber = searchParams.get('pageNumber');
    if (paramsPageNumber) {
      paramsPageNumber = parseInt(paramsPageNumber) - 1;
      if (paramsPageNumber == NaN) {
        return;
      }
    }

    if (paramsPageNumber != pageNumber.current) {
      dispatch(fetchAgents({ pageNumber: paramsPageNumber || 0, pageSize: 10 }));
      pageNumber.current = paramsPageNumber;
    } else {
      if (!agents) {
        dispatch(fetchAgents({ pageNumber: 0, pageSize: 10 }));
      } else {
        if (agents.totalPages < paramsPageNumber) {
          setSearchParams(params => {
            params.set('pageNumber', agents.totalPages);
            return params;
          })
        }
      }
    }
  }, [dispatch, searchParams, agents]);
  
  return agents;
};

export default useAgents;