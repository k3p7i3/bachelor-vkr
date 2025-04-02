import { Button } from "@mui/material";

export default function UnauthorizedNavBarContent() {
  return (
    <div>
      <Button color="inherit" disableRipple>Вход для агента</Button>
      <Button color="inherit" disableRipple>Вход для клиента</Button>
    </div>
  )
}