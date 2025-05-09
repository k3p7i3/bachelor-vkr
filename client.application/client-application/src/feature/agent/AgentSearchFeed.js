import { Stack, Typography, Pagination } from "@mui/material"; 
import { useSelector } from "react-redux";
import { useSearchParams } from "react-router-dom";

import AgentPreviewCard from "./AgentPreviewCard";
import CenteredMediumBox from "../../component/ui/CenteredMediumBox";
import { tariffs } from "./AgentPublicProfile";
import useAgents from "../../hook/useAgents";

export default function AgentSearchFeed() {
  const [searchParams, setSearchParams] = useSearchParams();
  const nameFilter = searchParams.get('nameFilter');

  const agents = useAgents();

  return <Stack>
    <CenteredMediumBox>
      <Typography variant="h4">Посредники</Typography>
    </CenteredMediumBox>
    {agents &&
      <Stack spacing={4}>
        {agents.content.map((agent) => {
          return <AgentPreviewCard agent={agent} tariffs={tariffs}/>;
        })}
        <Pagination count={agents.totalPages} sx={{ alignSelf: 'center'}}></Pagination>
      </Stack>
    }
  </Stack>
};