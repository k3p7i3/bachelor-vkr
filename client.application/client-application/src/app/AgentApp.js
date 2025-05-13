import { Route, Routes } from "react-router-dom";
import BasicLayout from "../component/BasicLayout";
import HomePageContent from "../feature/HomePageContent";
import TariffCreateForm from "../feature/tariff/form/TariffCreateForm";
import UserProfile from "../feature/user/UserProfile";
import AgentNavBar from "../component/AgentNavBarContent";
import AgentProfile from "../feature/agent/AgentProfile";
import OrderList from "../feature/order/OrderList";
import EditableOrderCard from "../feature/order/editable/EditableOrderCard";

function AgentApp() {
  return <Routes>
    <Route path="" element={<BasicLayout header={<AgentNavBar/>}/>}>
      <Route path="" element={<HomePageContent/>}/>

      <Route path="profile" element={<UserProfile isClient={false}/>}/>
      <Route path="agent/profile" element={<AgentProfile/>}/>

      <Route path='orders' element={<OrderList/>}/>
      <Route path='order/:orderId' element={<EditableOrderCard/>}/>

      <Route path='agent/create-tariff/' element={<TariffCreateForm/>}/>
    </Route>
  </Routes>
};

export default AgentApp;