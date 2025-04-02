import CenteredCard from "./CenteredCard";
import { styled } from '@mui/material/styles';

const CenteredLargeCard = styled(CenteredCard)(({ theme }) => ({
  width: '100%',
  margin: 'auto',
  [theme.breakpoints.up('sm')]: {
    maxWidth: '1400px',
  },
}));

export default CenteredLargeCard;