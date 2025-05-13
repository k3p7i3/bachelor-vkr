import { Box } from "@mui/material";
import { styled } from '@mui/material/styles';

const CenteredLargeBox = styled(Box)(({ theme }) => ({
  display: 'flex',
  flexDirection: 'column',
  alignSelf: 'center',
  width: '100%',
  padding: theme.spacing(4),
  gap: theme.spacing(2),
  margin: 'auto',
  [theme.breakpoints.up('sm')]: {
    maxWidth: '1400px',
  },
}));

export default CenteredLargeBox;