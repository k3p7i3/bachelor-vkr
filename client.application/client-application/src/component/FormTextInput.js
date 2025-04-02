import { Controller } from "react-hook-form";
import { TextField } from "@mui/material";

export default function FormTextInput({
  field: {name, text, label, type, required, pattern}, 
  control
}) {
  return (
    <Controller
      name={name}
      control={control}
      rules={{
        required: required && "Обязательное поле",
        pattern: {
          value: pattern,
          message: "Неверный формат данных"
        }
      }}
      render={({
        field: { onChange, value },
        fieldState: { error },
        formState,
      }) => (
        <TextField
          value={value}
          type={type}
          placeholder={text}
          required={!!required}
          onChange={onChange}
          error={!!error}
          helperText={error ? error.message : null}
          fullWidth
          size="small"
          variant="outlined"
        />
      )}
    />
  );
}