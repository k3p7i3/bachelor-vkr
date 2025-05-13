import { Stack, Typography, Pagination } from "@mui/material"; 
import { useSelector } from "react-redux";
import { useSearchParams } from "react-router-dom";

import AgentPreviewCard from "./AgentPreviewCard";
import CenteredMediumBox from "../../component/ui/CenteredMediumBox";
import { tariffs } from "./AgentPublicProfile";
import useAgents from "../../hook/useAgents";

export default function AgentSearchFeed() {
  const [searchParams, setSearchParams] = useSearchParams();

  const page = searchParams.get('pageNumber') || '1';
  const pageNum = parseInt(page);
  const nameFilter = searchParams.get('nameFilter');

  const handleChange = (e, value) => {
    setSearchParams(params => {
      params.set('pageNumber', value);
      return params;
    })
  }

  const agents = useAgents();

  return <Stack>
    <CenteredMediumBox>
      <Typography variant="h4">Посредники</Typography>
    </CenteredMediumBox>
    {agents && agents.tariffs &&
      <Stack spacing={4}>
        {agents.agents.content.map((agent) => {
          return <AgentPreviewCard 
            agent={agent} 
            tariffs={agents.tariffs.tariffs.find((it) => (it.agentId === agent.profile.id)).tariffs}
          />;
        })}
        {agents?.agents &&
          <Pagination count={agents.agents.totalPages} page={pageNum} onChange={handleChange} sx={{ alignSelf: 'center'}}></Pagination>
        }

      </Stack>
    }
  </Stack>
};