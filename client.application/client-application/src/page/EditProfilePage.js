import CenteredSmallCard from "../component/ui/CenteredSmallCard";
import Form from "../component/Form";
import BasicLayout from "../component/ui/BasicLayout";
import NavBarBase from "../component/NavBarBase";
import UnauthorizedNavBarContent from "../component/UnauthorizedNavBarContent";

function EditProfileForm() {
  const title = {
    text: "Редактировать профиль"
  }

  const fields = [
    {
      fields: [
        {
          name: 'name',
          label: 'Имя',
          text: 'Введите имя',
          type: 'text',
          required: true,
          pattern: '.*'
        },
        {
          name: 'email',
          label: 'Электронная почта (e-mail)',
          text: 'Введите e-mail',
          type: 'email',
          required: true,
          pattern: '.*'
        }
      ]
    }
  ]

  const submit = {
    text: "Сохранить измененения",
    style: '',
    action: () => {}
  }

  return (
    <CenteredSmallCard spacing={4} sx={{alignItems: "center"}}>
      <Form title={title} fields={fields} submit={submit}/>
     </CenteredSmallCard>
  ) 
};

export default function EditProfilePage() {
  return (
    <BasicLayout
      header={
        <NavBarBase 
          barContent={<UnauthorizedNavBarContent/>}
        />      
      }
      content={<EditProfileForm/>}
    />
  )
};