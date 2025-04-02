import { TextField, styled } from "@mui/material";

const DenseTextField = styled(TextField)(({ theme }) => ({
  maxWidth: '60px',
  "& .MuiOutlinedInput-input": {
    padding: "4px 8px",
  },
  "& .MuiInputLabel-root": {
    top: -4
  }
}));

export default DenseTextField;