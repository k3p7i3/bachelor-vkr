import { ButtonGroup, Button, TextField } from "@mui/material"


function CountEnterTextField({count, setCount}) {
  const intRegex = /^\d*$/;

  const enterCount = (event) => {
    const inputValue = event.target.value;
    if (inputValue && intRegex.test(inputValue)) {
      setCount(parseInt(inputValue));
    }
  };

  return <TextField
    size="small"
    required
    inputProps={{ style: {textAlign: 'center'} }}
    sx={{
      textAlign: "center",  
      maxWidth: "3.5em",
      textAlign: "center",
      '& .MuiOutlinedInput-root': {
        borderRadius: 0,
      },
      fieldset: { borderColor: "var(--mui-palette-primary-light)" }
    }}
    value={count}
    onChange={enterCount}
  />
    
}

function Counter({count, setCount}) {
  const decreaseCount = (cnt) => {
    if (cnt > 0) {
      setCount(cnt);
    }
  }

  return <ButtonGroup size="small">
    <Button onClick={() => decreaseCount(count - 1)}>–</Button>
    <CountEnterTextField count={count} setCount={setCount}/>
    <Button onClick={() => setCount((count || 0) + 1)}>+</Button>
  </ButtonGroup>
};

export default Counter;
