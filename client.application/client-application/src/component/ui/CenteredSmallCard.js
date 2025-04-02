import CenteredCard from "./CenteredCard";
import { styled } from '@mui/material/styles';

const CenteredSmallCard = styled(CenteredCard)(({ theme }) => ({
  width: '100%',
  margin: 'auto',
  [theme.breakpoints.up('sm')]: {
    maxWidth: '450px',
  },
}));

export default CenteredSmallCard;