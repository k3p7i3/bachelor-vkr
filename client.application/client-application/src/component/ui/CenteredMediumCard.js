import CenteredCard from "./CenteredCard";
import { styled } from '@mui/material/styles';

const CenteredMediumCard = styled(CenteredCard)(({ theme }) => ({
  width: '100%',
  margin: 'auto',
  [theme.breakpoints.up('sm')]: {
    maxWidth: '800px',
  },
}));

export default CenteredMediumCard;