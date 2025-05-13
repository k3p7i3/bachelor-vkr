import { Route, Routes } from "react-router-dom";
import ClientNavBar from "../component/ClientNavBarContent";
import BasicLayout from "../component/BasicLayout";
import HomePageContent from "../feature/HomePageContent";
import UserProfile from "../feature/user/UserProfile";
import AgentSearchFeed from "../feature/agent/AgentSearchFeed";
import AgentPublicProfile from "../feature/agent/AgentPublicProfile";
import CartProductsFeed from "../feature/product/CartProductsFeed";
import CreateOrderForm from "../feature/order/form/CreateOrderForm";
import OrderCard from "../feature/order/static/OrderCard";
import OrderList from "../feature/order/OrderList";

function ClientApp() {
  return <Routes>
    <Route path="" element={<BasicLayout header={<ClientNavBar/>}/>}>
      <Route path="" element={<HomePageContent/>}/>

      <Route path="profile" element={<UserProfile isClient/>}/>

      <Route path='agents/' element={<AgentSearchFeed/>}/>

      <Route path='agents/:agentId' element={<AgentPublicProfile/>}/>

      <Route path='cart' element={<CartProductsFeed/>}/>

      <Route path='orders' element={<OrderList/>}/>

      <Route path='order/create' element={<CreateOrderForm/>}/>

      <Route path='order/:orderId' element={<OrderCard/>}/>
    </Route>
  </Routes>
};

export default ClientApp;