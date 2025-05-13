import { Grid2, Typography, Button, Stack } from "@mui/material";
import CenteredMediumCard from "../component/ui/CenteredMediumCard";
import { useNavigate } from "react-router-dom";

function HomePageContent() {
  const navigate = useNavigate();
  
  return <CenteredMediumCard>
    <Stack spacing={5}>
      <Typography variant="h3">Поможем найти посредника для шоппинга на Taobao и 1688</Typography>

      <Grid2 container direction='row' spacing={5} sx={{justifyContent: 'space-evenly'}}>
        <Grid2>
          <img 
            src="https://icons.veryicon.com/png/o/internet--web/color-social-media-icon/taobao-2.png" 
            style={{width: '250px'}}
          />
        </Grid2>

        <Grid2>
          <img
            src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQgBuPcBBLEZqUG5Kpgx0cNOWI5XJVEpbczIw&s"
            style={{width: '250px'}}
          />
        </Grid2>

      </Grid2>

      <Button 
        size='large' 
        variant="contained"
        sx={{height: '60px'}}
        onClick={(e) => navigate("/agents/")}
      >
        Найти специалиста
      </Button>
    </Stack>

  </CenteredMediumCard>
};

export default HomePageContent;