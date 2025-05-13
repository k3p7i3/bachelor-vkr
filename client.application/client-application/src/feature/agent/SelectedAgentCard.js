import { Typography, Paper, Avatar, Chip, Stack, Button } from "@mui/material";
import { useDispatch } from "react-redux";
import { removeAgentSelection } from "./agentSlice";

const getScaledChipSx = (scale, fontScale) => {
  const scaledChipSx = {
    height: `${scale * 32}px`,
    borderRadius: `${scale * 16}px`,
    '& .MuiChip-label': {
      paddingRight: `${scale * 12}px`,
      paddingLeft: `${scale * 12}px`,
      fontSize: `${fontScale * 0.8125}rem`,
    },
    '& .MuiChip-avatar': {
      width: `${scale * 24}px`,
      height: `${scale * 24}px`,
      fontSize: `${scale * 0.75}rem`,
    },
    '& .MuiChip-deleteIcon': {
      width: `${scale * 1}rem`,
      height: `${scale * 1}rem`
    }
  };
  return scaledChipSx;
}

export default function SelectedAgentCard({ userId, selectedAgent, canDelete = true }) {
  const dispatch = useDispatch();

  const avatar = (selectedAgent?.avatar?.presignedUrl 
    ? <Avatar src={selectedAgent?.avatar?.presignedUrl}/>
    : <Avatar>{selectedAgent?.name[0]}</Avatar>
  )

  const handleDelete = (e) => {
    e.preventDefault();
    dispatch(removeAgentSelection({
      userId: userId
    }))
  }

  return <Stack spacing={1} sx={{alignItems: 'center'}}>
    <Typography variant="body1">
      {selectedAgent ? 'Выбранный посредник:' : 'Посредник для заказа не выбран.'}
    </Typography>
    {selectedAgent ?
      <Chip
        avatar={avatar}
        component="a"
        label={selectedAgent.name}
        href={"/agents/" + selectedAgent.agentId}
        onDelete={canDelete ? handleDelete : undefined}
        variant="filled"
        clickable
        sx={getScaledChipSx(1.75, 1.4)}
      />
      : <Button href="/agents/">
        Найти посредника для заказа
      </Button>
    }
    
  </Stack>
};