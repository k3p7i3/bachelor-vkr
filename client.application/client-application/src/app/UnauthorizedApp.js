import { Route, Routes } from "react-router-dom";
import BasicLayout from "../component/BasicLayout";
import UnauthorizedNavBar from "../component/UnauthorizedNavBar";
import HomePageContent from "../feature/HomePageContent";
import AgentSearchFeed from "../feature/agent/AgentSearchFeed";
import AgentPublicProfile from "../feature/agent/AgentPublicProfile";
import UserLogin from "../feature/user/UserLogin";
import UserRegistrationForm from "../feature/user/UserRegistrationForm";
import AgentRegistrationForm from "../feature/agent/AgentRegistrationForm";

function UnauthorizedApp() {
  return <Routes>
    <Route path="" element={<BasicLayout header={<UnauthorizedNavBar/>}/>}>
      <Route path="" element={<HomePageContent/>}/>

      <Route path='agents/' element={<AgentSearchFeed/>}/>

      <Route path='agents/:agentId' element={<AgentPublicProfile/>}/>

      <Route path='/login' element={<UserLogin/>}/>

      <Route path='client/registration' element={<UserRegistrationForm/>}/>

      <Route path='agent/registration' element={<AgentRegistrationForm/>}/>
    </Route>
  </Routes>
};

export default UnauthorizedApp;