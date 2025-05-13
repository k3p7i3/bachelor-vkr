import { Select, styled } from "@mui/material";

const DenseSelectField = styled(Select)(({ theme }) => ({
  "& .MuiSelect-select": {
    padding: "4px 8px"
   }
}));

export default DenseSelectField;