import { AppBar, Toolbar, Box, Typography, Button } from "@mui/material";


export default function NavBarBase({ barContent }) {
  return (
    <Box sx={{ flexGrow: 1, height: 'var(--header-height, 0)' }}>
      <AppBar>
        <Toolbar variant="dense" sx={{justifyContent: 'space-between'}}>
            <Button color="inherit" disableRipple>
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