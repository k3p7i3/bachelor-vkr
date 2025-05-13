import CenteredMediumCard from '../../component/ui/CenteredMediumCard';
import Form from "../../component/Form";
import {Link as RouterLink} from "react-router-dom";
import Link from '@mui/material/Link';
import { useDispatch } from 'react-redux';
import { registerAgent } from '../../api/agentApi';

function AgentRegistrationForm() {
  const dispatch = useDispatch();
  const title = {
    text: "Регистрация посредника"
  }

  const fields = [
    {
      section: 'Данные посредника',
      about: 'Данные юридического лица / самозанятого посредника, которые будут видны всем пользователям',
      fields: [
        {
          name: 'name',
          label: 'Название / имя',
          text: 'Введите название / имя',
          type: 'text',
          helperText: 'Если оставить пустым - используется имя вашего личного аккаунта',
          required: false,
        },
        {
          name: 'legalName',
          label: 'Юридическое наименование',
          text: 'Введите наименование (если есть)',
          type: 'text',
          required: false
        },
        {
          name: 'contactPhoneNumber',
          label: 'Контактный номер телефона',
          text: 'Введите номер телефона',
          type: 'text',
          helperText: 'Опционально, можно оставить пустым',
          required: false,
          pattern: /^[\+]?[(]?[0-9]{3}[)]?[-\s\.]?[0-9]{3}[-\s\.]?[0-9]{4,6}$/
        },
        {
          name: 'contactEmail',
          label: 'Контактный e-mail',
          text: 'Введите электронную почту (если есть)',
          type: 'email',
          helperText: 'Опционально, можно оставить пустым',
          required: false,
          pattern: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/
        }
      ]
    },
    {
      section: 'Данные личного аккаунта',
      about: 'Данные личного аккаунта, которые не будут видны клиентам',
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
          required: true
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
          pattern: '.*'
        },
        {
          name: 'repeatPassword',
          label: 'Повторите пароль',
          text: 'Введите пароль',
          type: 'password',
          required: true,
          pattern: '.*',
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
    action: (event) => {
      console.log(event)
      dispatch(registerAgent({
        agentProfile: {
          name: event.name,
          contactPhoneNumber: event.contactPhoneNumber,
          contactEmail: event.contactEmail,
          legalName: event.legalName
        },
        userData: {
          firstName: event.firstName,
          lastName: event.lastName,
          email: event.email,
          password: event.password,
          role: "AGENT_ADMIN"
        }
      }))
    }
  }

  return (
    <CenteredMediumCard spacing={4} sx={{alignItems: "center"}}>
      <Form title={title} fields={fields} submit={submit}/>
      <Link component={RouterLink} to="/login" underline="hover">
        Уже есть аккаунт? Войти
      </Link>
    </CenteredMediumCard>
  ) 
}

export default AgentRegistrationForm;