import { Button, FormControl, FormLabel, Typography, Stack } from "@mui/material";
import Grid from "@mui/material/Grid2";
import React, { useEffect } from "react";
import { useForm, Controller } from "react-hook-form";
import FormTextInput from "./FormTextInput";

const makePhoneNumberField = (field, register, error) => {
  var validation = {
    required: field.required && "Обязательное поле",
    pattern: {
      value: /^[\+]?[(]?[0-9]{3}[)]?[-\s\.]?[0-9]{3}[-\s\.]?[0-9]{4,6}$/im,
      message: "Неверный формат данных"
    }
  }
  return <input key={field.text}
    {...register(field.name, validation)}
    type={field.type}
    placeholder={field.text}
    className={`${field.style}${error ? " error" : ""}`} />
}

const makeRadioField = (field, register, entity, error) => {
  var validation = {
    required: field.required
  }
  return (
    <>
      <h4>{field.text}</h4>
      {error && <span className="error-font">{error.message}</span>}
      {field.options.map((option, index) => {
        return (
          <label className="radio" key={option}>
            <input
              {...register(field.name, validation)}
              type='radio'
              defaultChecked={entity ? entity[field.name] === option : index === 0}
              className={field.style}
              value={option} />
            <span>{option}</span>
          </label>
        )
      })}
    </>
  )
}

const makeSelectField = (field, register, error) => {
  var validation = {
    required: field.required && "Обязательное поле",
  }
  return (
    <>
      <h4>{field.text}</h4>
      {error && <span className="error-font">{error.message}</span>}
      <select
        key={field.name} {...register(field.name, validation)}>
        {field.options.map((option) => {
          return (
            <option key={option} value={option}>{option}</option>
          )
        })}
      </select>
    </>
  )
}


// const makeField = (field, register, error) => {
//   var validation = {
//     required: field.required && "Обязательное поле",
//     pattern: {
//       value: field.pattern,
//       message: "Неверный формат данных"
//     }
//   }
//   return <input key={field.name}
//     {...register(field.name, validation)}
//     type={field.type}
//     placeholder={field.text}
//     className={`${field.style}${error ? " error" : ""}`} />
// }

/*
    fields:
    [
        {
            name: string,
            label: string,
            text: string,
            type: string,
            style: string,
            required: boolean,
            pattern: regex,
            options: [string],
        }
    ],
    submit: {
        text,
        style,
        action,
    },
    entity: {
        name: stirng,
        value: string,
    }
*/

const getFormField = (field, control, errors) => {
  let fieldInput = FormTextInput({ field, control })
  
  return (
    <FormControl fullWidth>
      <FormLabel htmlFor={field.name}>{field.label}</FormLabel>
      {fieldInput}
    </FormControl>
  );
}



function Form({ title, fields, submit, entity }) {
  const { control, handleSubmit, reset, formState: { errors } } = useForm();

  useEffect(() => {
    reset(entity);
  }, [reset, entity]);

  const onSubmit = (data) => {
    submit.action(data);
  }

  return (
    <form>
      <Stack spacing={4} sx={{alignItems: "center"}}>

      <Typography variant="h4" component={!!title.size}>{title.text}</Typography>

      {fields &&
        fields.map((section) => {
          return (
            <Stack key={section.section} spacing={3} sx={{alignItems: "center"}}>
              {section.section && <Typography variant="h6" component={!!section.size}>{section.section}</Typography>}
              
              {section.about && <Typography variant="subtitle1">{section.about}</Typography>}

              <Grid container rowSpacing={3} columnSpacing={{ xs: 1, sm: 2, md: 3 }}>
                {
                  section.fields.map((field) => {
                    // return getFormField(field, register, entity, errors);
                    return (
                      <Grid 
                        size={{
                          xs: 12, sm: 12, md: 12 / (section.columnNumber ? section.columnNumber : 1)
                        }}
                      >
                        {getFormField(field, control)}
                      </Grid>
                    )
                  })
                }
              </Grid>
            </Stack>
          )
        })}

        <Button fullWidth onClick={handleSubmit(onSubmit)} variant="contained">{submit.text}</Button>

      </Stack>
      
    </form>
  )
}

export default Form;