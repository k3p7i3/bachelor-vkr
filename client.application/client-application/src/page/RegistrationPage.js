import BasicLayout from "../component/ui/BasicLayout";
import NavBarBase from "../component/NavBarBase";
import UserRegistrationForm from "../feature/user/UserRegistrationForm";
import UnauthorizedNavBarContent from "../component/UnauthorizedNavBarContent";

export default function RegistrationPage() {
  return (
    <BasicLayout
      header={
        <NavBarBase 
          barContent={<UnauthorizedNavBarContent/>}
        />      
      }
      content={<UserRegistrationForm/>}
    />
  )
};