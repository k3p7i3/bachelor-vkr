import { Paper, InputBase, IconButton, Stack } from "@mui/material";
import SearchIcon from '@mui/icons-material/Search';

const SearchBar = ({placeholder, onSubmit}) => {
  return <Paper component="form" onSubmit={onSubmit} sx={{ p: '0px 7px', borderRadius: 3 }}>
    <Stack direction="row">
      <IconButton><SearchIcon/></IconButton>
      <InputBase 
        id="search" 
        placeholder={placeholder} 
        inputProps={{ 'aria-label': 'search' }}
      />
    </Stack>

  </Paper>
};

export default SearchBar;
