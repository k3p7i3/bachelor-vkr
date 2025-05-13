import { AppBar, Toolbar, Box, Typography, Button, Grid2 } from "@mui/material";

import { useNavigate } from "react-router-dom";

export default function NavBarBase({ barContent }) {
  const navigate = useNavigate();

  const navigateToHomePage = () => {
    navigate('/')
  }

  return (
    <Box sx={{ flexGrow: 1, height: 'var(--header-height, 0)' }}>
      <AppBar position="static">
        <Toolbar sx={{ justifyContent: 'space-between'}}>
          <Button color="inherit" disableRipple onClick={navigateToHomePage}>
            <Typography variant="h5" component="div">
              China Town
            </Typography>
          </Button>

          {barContent}

        </Toolbar>
      </AppBar>
    </Box>
  )
};