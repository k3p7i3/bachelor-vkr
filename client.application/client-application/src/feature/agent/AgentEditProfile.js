import CenteredMediumCard from '../../component/ui/CenteredMediumCard';
import Form from "../../component/Form";
import { useDispatch } from 'react-redux';
import { useState } from 'react';
import { Button, Dialog } from '@mui/material';
import { updateAgentProfile } from './agentSlice';

function AgentEditProfileForm({agent, setIsOpen}) {
  const dispatch = useDispatch();

  const title = {
    text: "Редактирование профиля посредника"
  }

  const fields = [
    {
      fields: [
        {
          name: 'name',
          label: 'Название / имя',
          text: 'Введите название / имя',
          type: 'text',
          helperText: 'Если оставить пустым - используется имя вашего личного аккаунта',
          required: false,
          defaultValue: agent.name
        },
        {
          name: 'legalName',
          label: 'Юридическое наименование',
          text: 'Введите наименование (если есть)',
          type: 'text',
          required: false,
          defaultValue: agent.legalName
        },
        {
          name: 'contactPhoneNumber',
          label: 'Контактный номер телефона',
          text: 'Введите номер телефона',
          type: 'text',
          helperText: 'Опционально, можно оставить пустым',
          required: false,
          pattern: /^[\+]?[(]?[0-9]{3}[)]?[-\s\.]?[0-9]{3}[-\s\.]?[0-9]{4,6}$/,
          defaultValue: agent.contactPhoneNumber
        },
        {
          name: 'contactEmail',
          label: 'Контактный e-mail',
          text: 'Введите электронную почту (если есть)',
          type: 'email',
          helperText: 'Опционально, можно оставить пустым',
          required: false,
          pattern: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
          defaultValue: agent.contactEmail
        },
        {
          name: 'description',
          label: 'Описание',
          text: 'Введите описание',
          type: 'text',
          helperText: 'Опционально, можно оставить пустым',
          required: false,
          defaultValue: agent.description,
          rows: 5
        }
      ]
    }
  ]

  const submit = {
    text: "Сохранить измененения",
    style: '',
    action: (e) => {
      Object.keys(e).forEach((key) => {
        if (e[key] == '' || !e[key]) {
          e[key] = null
        }
      })
      e.id = agent.id
      dispatch(updateAgentProfile({agentProfile: e}))
      setIsOpen(false);
    }
  }

  return (
    <CenteredMediumCard spacing={4} sx={{alignItems: "center"}}>
      <Form title={title} fields={fields} submit={submit}/>
     </CenteredMediumCard>
  )
}

const AgentEditProfile = ({agent}) => {
  const [isOpen, setIsOpen] = useState(false);
  return <>
    <Button variant="contained" size="large" onClick={() => setIsOpen(true)}>Редактировать профиль</Button>

    <Dialog
      open={isOpen}
      onClose={() => setIsOpen(false)}
    >
      <AgentEditProfileForm agent={agent.profile} setIsOpen={setIsOpen}/>
    </Dialog>
  </>
};

export default AgentEditProfile;