import Link from '@mui/material/Link';
import Form from "../../component/Form";
import CenteredSmallCard from '../../component/ui/CenteredSmallCard';
import {Link as RouterLink, useNavigate} from "react-router-dom"
import { useDispatch } from 'react-redux';
import { registerUser } from './userSlice';

function UserRegistrationForm() {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const title = {
    text: "Регистрация клиента"
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
          name: 'lastName',
          label: 'Фамилия',
          text: 'Введите фамилию',
          type: 'text',
          required: false
        },
        {
          name: 'email',
          label: 'Электронная почта (e-mail)',
          text: 'Введите e-mail',
          type: 'email',
          required: true,
          pattern: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/
        },
        {
          name: 'password',
          label: 'Пароль',
          text: 'Введите пароль',
          type: 'password',
          required: true,
        },
        {
          name: 'repeatPassword',
          label: 'Повторите пароль',
          text: 'Введите пароль',
          type: 'password',
          required: true,
          validate: (repeatPassword, { password }) => {
            return (repeatPassword == password) || "Пароли должны совпадать"
          }
        }
      ]
    }
  ]

  const submit = {
    text: "Зарегистрироваться",
    style: '',
    action: (e) => {
      Object.keys(e).forEach((key) => {
        if (e[key] == '' || !e[key]) {
          e[key] = null
        }
      })
      dispatch(registerUser({
        firstName: e.firstName,
        lastName: e.lastName,
        email: e.email,
        password: e.password
      }))
      navigate('/')
    }
  }

  return (
    <CenteredSmallCard spacing={4} sx={{alignItems: "center"}}>
      <Form title={title} fields={fields} submit={submit}/>
      <Link component={RouterLink} to="/login" underline="hover">
        Уже есть аккаунт? Войти
      </Link>
    </CenteredSmallCard>
  ) 
}

export default UserRegistrationForm;