
const numericConditionTypesList = ["WEIGHT", "VOLUME", "DENSITY", "UNITS", "PRICE"];

const numericConditionTypes = [
  {
    value: "WEIGHT",
    label: "Вес"
  },
  {
    value: "VOLUME",
    label: "Объем"
  },
  {
    value: "DENSITY",
    label: "Плотность"
  },
  {
    value: "UNITS",
    label: "Количество товаров"
  },
  {
    value: "PRICE",
    label: "Стоимость"
  }
];

const numericConditionTypeToMeasurements = {
  "WEIGHT": [
    {
      value: "KILOGRAM",
      fullLabel: "килограмм",
      label: "кг"
    },
    {
      value: "GRAM",
      fullLabel: "грамм",
      label: "г"
    },
    {
      value: "TON",
      fullLabel: "тонна",
      label: "т"
    },
    {
      value: "POUND",
      fullLabel: "фунт",
      label: "lb"
    },
    {
      value: "OUNCE",
      fullLabel: "унция",
      label: "oz"
    }
  ],

  "VOLUME": [
    {
      value: "CUBIC_METER",
      fullLabel: "метр³",
      label: "м³"
    },
    {
      value: "LITER",
      fullLabel: "литр",
      label: "л"
    },
    {
      value: "MILLILITER",
      fullLabel: "миллилитр",
      label: "мл"
    },
    {
      value: "GALLON",
      fullLabel: "галлон",
      label: "gal"
    },
    {
      value: "QUART",
      fullLabel: "кварта",
      label: "qt"
    },
    {
      value: "CUBIC_FOOT",
      fullLabel: "фут³",
      label: "ft³"
    }
  ],

  "DENSITY": [
    {
      value: "KG_PER_CUBIC_METER",
      fullLabel: "килограмм на метр³",
      label: "кг/м³"
    },
    {
      value: "GRAM_PER_CUBIC_CM",
      fullLabel: "грамм на сантиметр³ ",
      label: "г/см³"
    },
    {
      value: "KG_PER_CUBIC_CM",
      fullLabel: "килограмм на сантиметр³",
      label: "кг/см³"
    },
    {
      value: "GRAM_PER_CUBIC_METER",
      fullLabel: "грамм на метр³",
      label: "г/м³"
    },
    {
      value: "POUNDS_PER_CUBIC_FOOT",
      fullLabel: "фунт на фут³",
      label: "lb/ft³"
    } 
  ],

  "UNITS": [],

  "PRICE": [
    {
      value: "RUB",
      fullLabel: "российский рубль",
      label: "₽"
    },
    {
      value: "CNY",
      fullLabel: "китайский юань",
      label: "¥"
    },
    {
      value: "USD",
      fullLabel: "долллар США",
      label: "$"
    },
    {
      value: "EUR",
      fullLabel: "евро",
      label: "€"
    }
  ],

  "LENGTH": [
    {
      value: "METRE",
      label: "м"
    },
    {
      value: "CENTIMETRE",
      label: "см"
    },
    {
      value: "MILLIMETRE",
      label: "мм"
    },
    {
      value: "FOOT",
      label: "ft"
    },
    {
      value: "INCH",
      label: "дюймов"
    }
  ]
};

const perUnitTypeList = [
  {
    value: "PRODUCT_NUM",
    label: "за единицу товара"
  },
  {
    value: "PRODUCT_TYPE_UNIT",
    label: "за каждое наименование"
  },
  {
    value: "FIXED",
    label: "за весь заказ"
  },
  {
    value: "PERCENTAGE",
    label: "процент от стоимости"
  },
  {
    value: "WEIGHT",
    label: "за единицу веса"
  },
  {
    value: "VOLUME",
    label: "за единицу объема"
  },
  // {
  //   value: "DENSITY",
  //   label: "за единицу плотности"
  // }
];

const features = [
  {
    featureId: 1,
    title: 'Доставка',
    options: [
      {
        optionId: 1,
        title: 'Авто'
      },
      {
        optionId: 2,
        title: 'Поезд ж/д'
      },
      {
        optionId: 3,
        title: 'Авиа'
      }
    ]
  },
  {
    featureId: 2,
    title: 'Тип товаров',
    options: [
      {
        optionId: 1,
        title: 'Одежда'
      },
      {
        optionId: 2,
        title: 'Хозяйственное'
      },
      {
        optionId: 3,
        title: 'Техника'
      }
    ]
  }
];

export { 
  numericConditionTypesList,
  numericConditionTypes, 
  numericConditionTypeToMeasurements,
  perUnitTypeList,
  features
};