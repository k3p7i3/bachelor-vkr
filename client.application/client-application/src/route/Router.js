import {
    BrowserRouter,
    Routes,
    Route,
    Navigate
} from "react-router-dom";
import LoginPage from "../page/LoginPage";
import RegistrationPage from "../page/RegistrationPage";
import AgentProfile from "../feature/agent/AgentProfile";
import AgentSearchFeed from "../feature/agent/AgentSearchFeed";
import TariffCreateForm from "../feature/agent/tariff/form/TariffCreateForm";
import EditProfilePage from "../page/EditProfilePage";

export function Router() {
  return (
    <BrowserRouter>
      <Routes>

      <Route path='client/login' element={<LoginPage />} />
      <Route path='client/registration' element={<RegistrationPage />} />

      <Route path='client/edit' element={<EditProfilePage />} />

      <Route path='client/agent/:agentId' element={<AgentProfile/>}/>

      <Route path='client/agents/' element={<AgentSearchFeed/>}/>

      <Route path = 'agent/create-tariff/' element={<TariffCreateForm/>}/>


      </Routes>
    </BrowserRouter>
  )
}