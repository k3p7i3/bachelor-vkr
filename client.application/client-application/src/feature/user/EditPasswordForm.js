import { useDispatch } from "react-redux";
import { changePassword } from "./userSlice";
import CenteredSmallCard from "../../component/ui/CenteredSmallCard";
import Form from "../../component/Form";

export default function EditPasswordForm({setIsOpen, email}) {
  const dispatch = useDispatch();

  const title = {
    text: "Изменение пароля"
  }
  const fields = [
    {
      fields: [
        {
          name: 'oldPassword',
          label: 'Старый пароль',
          text: 'Введите старый пароль',
          type: 'password',
          required: true,
          pattern: '.*'
        },
        {
          name: 'newPassword',
          label: 'Новый пароль',
          text: 'Введите новый пароль',
          type: 'password',
          required: true,
          pattern: '.*'
        },
        {
          name: 'newPasswordAgain',
          label: 'Повторите пароль',
          text: 'Повторите новый пароль',
          type: 'password',
          required: true,
          pattern: '.*',
          validate: (newPasswordAgain, { newPassword }) => {
            return (newPasswordAgain == newPassword) || "Пароли должны совпадать"
          }
        }
      ]
    }
  ]

  const submit = {
    text: "Сохранить измененения",
    style: '',
    action: (e) => {
      dispatch(changePassword({
        email: email,
        oldPassword: e.oldPassword,
        newPassword: e.newPassword
      }));
      setIsOpen(false)
    }
  }

  return (
    <CenteredSmallCard spacing={4} sx={{alignItems: "center"}}>
      <Form title={title} fields={fields} submit={submit}/>
     </CenteredSmallCard>
  ) 
};