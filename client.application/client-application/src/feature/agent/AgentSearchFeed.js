import { Box, Stack, styled, Typography, Paper, InputBase, IconButton, Pagination } from "@mui/material";
import SearchIcon from '@mui/icons-material/Search';
import AgentPreviewCard from "./AgentPreviewCard";

import { agentInfo, tariffs } from "./AgentProfile";
import PageStackContainer from "../../component/ui/PageStackContainer";

const agents = [
  [agentInfo, tariffs],
  [agentInfo, tariffs],
  [agentInfo, tariffs],
  [agentInfo, tariffs],
  [agentInfo, tariffs],
  [agentInfo, tariffs],
  [agentInfo, tariffs],
  [agentInfo, tariffs],
  [agentInfo, tariffs],
];


const CenteredMediumBox = styled(Box)(({ theme }) => ({
  display: 'flex',
  flexDirection: 'column',
  alignSelf: 'center',
  width: '100%',
  padding: theme.spacing(2),
  gap: theme.spacing(2),
  margin: 'auto',
  width: '100%',
  [theme.breakpoints.up('sm')]: {
    maxWidth: '800px',
  }
}));

const SearchBar = ({placeholder, onSubmit}) => {
  return <Paper component="form" sx={{ p: 1, width: 'fit-content', borderRadius: 3 }}>
    <InputBase placeholder={placeholder}/>
    <IconButton><SearchIcon/></IconButton>
  </Paper>
};

export default function AgentSearchFeed() {
  return <PageStackContainer spacing={1}>

    <CenteredMediumBox>
      <SearchBar placeholder="Найти посредника"/>
    </CenteredMediumBox>

    <CenteredMediumBox>
      <Typography variant="h4">Посредники</Typography>
    </CenteredMediumBox>
    
    <Stack spacing={4}>
      {agents.map((agent) => {
        return <AgentPreviewCard agent={agent[0]} tariffs={agent[1]}/>;
      })}
      <Pagination count={10} sx={{ alignSelf: 'center'}}></Pagination>
    </Stack>
    
  </PageStackContainer>
};