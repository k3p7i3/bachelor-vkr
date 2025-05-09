import { createSelector } from "@reduxjs/toolkit";
import { selectAgentTariffs } from "../tariff/tariffSlice";
import { selectAuthAgent, selectCurrentAgent } from "./agentSlice";

export const selectAuthAgentInfo = createSelector(
  [selectAuthAgent, selectAgentTariffs],
  (agent, tariffs) => {
    if (!agent) return null;
    if (!tariffs || agent.agentId != tariffs.agentId) {
      return agent;
    }
  
    return {
      ...agent,
      tariffs: tariffs.tariffs
    };
  }
);


export const selectCurrentAgentInfo = createSelector(
  [selectCurrentAgent, selectAgentTariffs],
  (agent, tariffs) => {
    if (!agent) return null;
    if (!tariffs || agent.agentId != tariffs.agentId) {
      return agent;
    }
    return {
      ...agent,
      tariffs: tariffs.tariffs
    };
  }
);