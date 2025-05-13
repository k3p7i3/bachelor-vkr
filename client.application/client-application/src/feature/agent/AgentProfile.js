import { Stack } from "@mui/material";

import AgentInfo from "./AgentInfo";
import Tariffs from "../tariff/TariffInfo";
import useAuthAgent from "../../hook/useAuthAgent";

export default function AgentProfile() {
  const agent = useAuthAgent();

  return <Stack spacing={2}>
    {agent && <AgentInfo agent={agent.agent} canEdit/>}
    {agent?.tariffs && <Tariffs tariffs={agent.tariffs}/>}
  </Stack>
};