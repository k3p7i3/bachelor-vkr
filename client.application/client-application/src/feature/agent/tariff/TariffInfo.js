import React from "react";
import { Box, Tab, Tabs, Typography, Stack } from "@mui/material";
import TariffTable from "./TariffTable";
import CustomCarousel from "../../../component/ui/CustomCarousel";
import CenteredMediumCard from "../../../component/ui/CenteredMediumCard";

function TariffInfo({tariff, index, value}) {

  return (
    <div
      role="tabpanel"
      hidden={value !== index}
    >
      {value === index && <Stack spacing={2} sx={{padding: '10px 0'}}>

        <Typography variant="h6">{tariff.description}</Typography>
        <CustomCarousel items={{large: 1, medium: 1, small: 1}} slidesToSlide={1}>
          {tariff.tariffTables.map((tariffTable) => {
            return <TariffTable tariff={tariff} tariffTable={tariffTable}/>
          })}
        </CustomCarousel>


      </Stack>}
    </div>
  );
}

export default function Tariffs({tariffs}) {

  const [value, setValue] = React.useState(0);

  const handleChange = (event, newValue) => {
    setValue(newValue);
  };

  return <CenteredMediumCard>
    <Typography variant='h5'>Услуги посредника</Typography>
    <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
      <Tabs
        value={value}
        onChange={handleChange}
        variant="scrollable"
        scrollButtons="auto"
        aria-label="scrollable auto tabs example"
      >
        {tariffs.map((tariff) => {
          return <Tab label={tariff.title}/>
        })}
      </Tabs>

      {tariffs.map((tariff, index) => {
        return <TariffInfo tariff={tariff} index={index} value={value}/>
      })}
    </Box>
  </CenteredMediumCard>
}