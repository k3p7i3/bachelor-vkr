import { Box, styled } from "@mui/material";

const CenteredMediumBox = styled(Box)(({ theme }) => ({
  display: 'flex',
  flexDirection: 'column',
  alignSelf: 'center',
  width: '100%',
  padding: theme.spacing(2),
  gap: theme.spacing(2),
  margin: 'auto',
  width: '100%',
  [theme.breakpoints.up('sm')]: {
    maxWidth: '800px',
  }
}));

export default CenteredMediumBox;