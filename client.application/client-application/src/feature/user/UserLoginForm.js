import Form from "../../component/Form";
import CenteredSmallCard from "../../component/ui/CenteredSmallCard";
import Link from '@mui/material/Link';
import {Link as RouterLink} from "react-router-dom"

function LoginForm() {
  const title = {
    text: "Вход"
  }

  const fields = [
    {
      fields: [
        {
          name: 'email',
          label: 'Электронная почта (e-mail)',
          text: 'Введите e-mail',
          type: 'email',
          required: true,
          pattern: '.*'
        },
        {
          name: 'password',
          label: 'Пароль',
          text: 'Введите пароль',
          type: 'password',
          required: true,
          pattern: '.*'
        }
      ]
    }
  ]

  const submit = {
    text: "Войти",
    style: '',
    action: () => {}
  }

  return (
    <CenteredSmallCard spacing={4} sx={{alignItems: "center"}}>
      <Form title={title} fields={fields} submit={submit}/>
      <Link component={RouterLink} to="/client/registration" underline="hover">
        Нет аккаунта? Зарегистрироваться</Link>
      </CenteredSmallCard>
  ) 
}

export default LoginForm;