import { useDispatch } from "react-redux";
import { editUserInfo } from "./userSlice";
import CenteredSmallCard from "../../component/ui/CenteredSmallCard";
import Form from "../../component/Form";

export default function EditProfileForm({setIsOpen, user}) {
  const dispatch = useDispatch();
  const title = {
    text: "Редактировать профиль"
  }

  const fields = [
    {
      fields: [
        {
          name: 'firstName',
          label: 'Имя',
          text: 'Введите имя',
          type: 'text',
          required: true,
          pattern: '.*',
          defaultValue: user.firstName
        },
        {
          name: 'lastName',
          label: 'Фамилия',
          text: 'Введите фамилию',
          type: 'text',
          required: true,
          pattern: '.*',
          defaultValue: user.lastName
        },
        {
          name: 'email',
          label: 'Электронная почта (e-mail)',
          text: 'Введите e-mail',
          type: 'email',
          required: true,
          pattern: '.*@.*',
          defaultValue: user.email
        }
      ]
    }
  ]

  const submit = {
    text: "Сохранить измененения",
    style: '',
    action: (e) => {
      dispatch(editUserInfo({
        username: user.email,
        firstName: e.firstName,
        lastName: e.lastName,
        email: e.email
      }))
      setIsOpen(false);
    }
  }

  return (
    <CenteredSmallCard spacing={4} sx={{alignItems: "center"}}>
      <Form title={title} fields={fields} submit={submit}/>
     </CenteredSmallCard>
  ) 
};