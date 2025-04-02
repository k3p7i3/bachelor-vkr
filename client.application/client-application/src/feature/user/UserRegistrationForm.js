
import Link from '@mui/material/Link';
import Form from "../../component/Form";
import CenteredSmallCard from '../../component/ui/CenteredSmallCard';
import {Link as RouterLink} from "react-router-dom"

function UserRegistrationForm() {
  const title = {
    text: "Регистрация"
  }

  const fields = [
    {
      fields: [
        {
          name: 'firstName',
          label: 'Имя',
          text: 'Введите имя',
          type: 'text',
          required: true
        },
        {
          name: 'email',
          label: 'Электронная почта (e-mail)',
          text: 'Введите e-mail',
          type: 'email',
          required: true,
          pattern: '.*@.*'
        },
        {
          name: 'password',
          label: 'Пароль',
          text: 'Введите пароль',
          type: 'password',
          required: true,
          pattern: '.*'
        },
        {
          name: 'repeat-password',
          label: 'Повторите пароль',
          text: 'Введите пароль',
          type: 'password',
          required: true,
          pattern: '.*'
        }
      ]
    }
  ]

  const submit = {
    text: "Зарегистрироваться",
    style: '',
    action: () => {}
  }

  return (
    <CenteredSmallCard spacing={4} sx={{alignItems: "center"}}>
      <Form title={title} fields={fields} submit={submit}/>
      <Link component={RouterLink} to="/client/login" underline="hover">
        Уже есть аккаунт? Войти
      </Link>
    </CenteredSmallCard>
  ) 
}

export default UserRegistrationForm;