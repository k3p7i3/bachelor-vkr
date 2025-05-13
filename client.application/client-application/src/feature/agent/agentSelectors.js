import { createSelector } from "@reduxjs/toolkit";
import { selectAgentsBriefTariffs, selectAgentTariffs } from "../tariff/tariffSlice";
import { selectAgents, selectAuthAgent, selectCurrentAgent } from "./agentSlice";

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

export const selectAgentsInfo = createSelector(
  [selectAgents, selectAgentsBriefTariffs],
  (agents, tariffs) => {
    if (!agents) return null;
    if (!tariffs || agents.number != tariffs.page) {
      return {agents: agents}
    };
    return {
      agents: agents,
      tariffs: tariffs
    }
  }
)