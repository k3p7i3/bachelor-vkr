import { Controller } from "react-hook-form";
import { TextField } from "@mui/material";

export default function FormTextInput({
  field: {name, text, type, required, pattern, validate, helperText, rows}, 
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
        },
        validate: validate
      }}
      render={({
        field: { onChange, value },
        fieldState: { error },
      }) => (
        <TextField
          value={value}
          type={type}
          placeholder={text}
          required={!!required}
          onChange={onChange}
          error={!!error}
          helperText={error ? error.message : helperText}
          fullWidth
          size="small"
          variant="outlined"
          multiline={!!rows}
          rows={rows}
        />
      )}
    />
  );
}