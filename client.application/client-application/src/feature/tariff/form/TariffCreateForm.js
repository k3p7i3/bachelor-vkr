import { Divider, TextField, Button, Stack, Typography } from "@mui/material";
import CenteredLargeCard from "../../../component/ui/CenteredLargeCard";
import { useEffect, useState, useRef } from "react";
import PriceTableCreateForm from "./PriceTableCreateForm";

function TariffFeatureForm({feature}) {
  const optionId = useRef(0);

  const [title, setTitle] = useState(feature.title || '');
  const [description, setDescripton] = useState(feature.description || '');
  const [options, setOptions] = useState(feature.options || []);

  useEffect(() => {
    feature.title = title
    feature.description = description
    feature.options = options
  }, [title, description, options, feature])

  const addOption = () => {
    const newOption = {optionId: optionId.current++};
    setOptions([...options, newOption])
  };

  const deleteOption = (id) => {
    setOptions(options.filter((o) => o.optionId != id))
  };

  return <Stack spacing={1}>
    <TextField 
      label="Название категории" 
      value={title} 
      onChange={(e) => setTitle(e.target.value)}
    />
    <TextField 
      label="Описание категории" 
      multiline 
      value={description}
      onChange={(e) => setDescripton(e.target.value)}
    />

    {options.length != 0 && 
      <Stack spacing={2}>
        <Divider>Опции</Divider>

        {options.map((option, index) => (
          <div key={feature.featureId + '_' + option.optionId}>
            <div>
              <Typography>Опция {index + 1}</Typography>
              <Button onClick={() => deleteOption(option.optionId)}>- Удалить</Button>
            </div>
            <TariffOptionForm option={option}/> 
          </div>
          
        ))}
      </Stack>
    }

    <Button onClick={addOption}>+ Добавить опцию</Button>
  </Stack>;
}

function TariffOptionForm({option}) {

  const [title, setTitle] = useState(option.title || '');
  const [description, setDescripton] = useState(option.setDescripton || '');

  useEffect(() => {
    option.title = title
    option.description = description
  }, [title, description, option])

  return <Stack spacing={1}>
  <TextField 
    label="Название опции" 
    value={title} 
    onChange={(e) => setTitle(e.target.value)}
  />
  <TextField 
    label="Описание категории" 
    multiline 
    value={description}
    onChange={(e) => setDescripton(e.target.value)}
  />
</Stack>;
}



export default function TariffCreateForm() {
  const featureId = useRef(0);

  const [features, setFeatures] = useState([]);
  
  const [step, setStep] = useState(0);

  const addFeature = () => {
    const newFeature = {featureId: featureId.current++, options: []};
    setFeatures([...features, newFeature])
  };

  const deleteFeature = (id) => {
    setFeatures(features.filter((f) => f.featureId != id))
  };

  return <CenteredLargeCard>

    <Typography variant="h5">Конструктор тарифа</Typography>
    
    {step == 0 && <Stack>

      <TextField label="Название услуги"/>
      <TextField label="Описание услуги" multiline minRows={3}/>

      <Divider>Категории</Divider>

      <Stack spacing={3}>
        {features.map((feature, index) => (
          <div key={feature.featureId}>
            <div>
              <Typography>Категория {index + 1}</Typography>
              <Button onClick={() => deleteFeature(feature.featureId)}>- Удалить</Button>
            </div>
            <TariffFeatureForm feature={feature}/>
          </div>
        ))}
      </Stack>


      <Button onClick={addFeature}>+ Добавить категорию</Button>

      <Button onClick={() => setStep(1)}>Далее</Button>
    </Stack>}

    <Divider></Divider>


    {step > 0 && <PriceTableCreateForm step={step} setStep={setStep}></PriceTableCreateForm>}

  </CenteredLargeCard>
};