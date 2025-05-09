import { Route, Routes } from "react-router-dom";
import BasicLayout from "../component/BasicLayout";
import HomePageContent from "../feature/HomePageContent";
import TariffCreateForm from "../feature/tariff/form/TariffCreateForm";
import UserProfile from "../feature/user/UserProfile";
import AgentNavBar from "../component/AgentNavBarContent";
import AgentProfile from "../feature/agent/AgentProfile";

function AgentApp() {
  return <Routes>
    <Route path="" element={<BasicLayout header={<AgentNavBar/>}/>}>
      <Route path="" element={<HomePageContent/>}/>

      <Route path="profile" element={<UserProfile isClient={false}/>}/>
      <Route path="agent/profile" element={<AgentProfile/>}/>

      <Route path = 'agent/create-tariff/' element={<TariffCreateForm/>}/>
    </Route>
  </Routes>
};

export default AgentApp;