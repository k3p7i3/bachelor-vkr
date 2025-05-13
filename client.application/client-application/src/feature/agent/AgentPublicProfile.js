import { useParams } from 'react-router-dom';
import AgentInfo from './AgentInfo';
import { Stack } from '@mui/material';
import Tariffs from '../tariff/TariffInfo';
import { useSelector } from 'react-redux';
import useCurrentAgent from '../../hook/useCurrentAgent';

// tariff info
export const tariffs = [
  {
    tariffId: "tariffId1",
    applyLevel: "ORDER",
    title: "Доставка",
    description: "Доставка",
    features: [
      {
        id: "featureId",
        title: "Категория",
        description: "Все товары в заказе должны принадлежать одной категории",
        options: [
          {
            id: "optionId1",
            title: "Одежда",
            description: "Не включает обувь, аксессуары"
          },
          {
            id: "optionId2",
            title: "Хозяйственные",
            description: "Утварь для дома"
          },
          {
            id: "optionId3",
            title: "Техника",
            description: "Компьютеры, гаджеты и т.д."
          },
        ]
      }
    ],
    tariffTables: [
      {
        commonConditions: [
          {
            type: "ENUM",
            typeCondition: {
              featureId: "featureId",
              optionId: "optionId1"
            },
            numericCondition: null
          },
          {
            type: "NUMERIC",
            typeCondition: null,
            numericCondition: {
              minLimit: 5,
              maxLimit: 100,
              measurementType: "PRICE",
              measurementUnit: "RUB"
            }
          }
        ],
        columns: [
          {
            type: "ENUM",
            typeCondition: {
              featureId: "featureId",
              optionId: "optionId1"
            },
            numericCondition: null
          },
          {
            type: "ENUM",
            typeCondition: {
              featureId: "featureId",
              optionId: "optionId2"
            },
            numericCondition: null
          },
          {
            type: "ENUM",
            typeCondition: {
              featureId: "featureId",
              optionId: "optionId3"
            },
            numericCondition: null
          },
        ],
        rows: [
          {
            type: "NUMERIC",
            typeCondition: null,
            numericCondition: {
              maxLimit: 100,
              measurementType: "WEIGHT",
              measurementUnit: "KILOGRAM"
            }
          },
          {
            type: "NUMERIC",
            typeCondition: null,
            numericCondition: {
              minLimit: 100,
              maxLimit: 200,
              measurementType: "WEIGHT",
              measurementUnit: "KILOGRAM"
            }
          },
          {
            type: "NUMERIC",
            typeCondition: null,
            numericCondition: {
              minLimit: 200,
              maxLimit: 300,
              measurementType: "WEIGHT",
              measurementUnit: "KILOGRAM"
            }
          },
          {
            type: "NUMERIC",
            typeCondition: null,
            numericCondition: {
              minLimit: 300,
              maxLimit: 400,
              measurementType: "WEIGHT",
              measurementUnit: "KILOGRAM"
            }
          },
          {
            type: "NUMERIC",
            typeCondition: null,
            numericCondition: {
              minLimit: 400,
              measurementType: "WEIGHT",
              measurementUnit: "KILOGRAM"
            }
          }
        ],
        tariffPrices: [
          [
            {
              price: {
                value: 3.1,
                unit: 'USD'
              },
              perUnit: {
                unitType: 'WEIGHT',
                unitMeasurement: 'KILOGRAM'
              }
            },
            {
              price: {
                value: 3.3,
                unit: 'USD'
              },
              perUnit: {
                unitType: 'WEIGHT',
                unitMeasurement: 'KILOGRAM'
              }
            },
            {
              price: {
                value: 4.5,
                unit: 'USD'
              },
              perUnit: {
                unitType: 'WEIGHT',
                unitMeasurement: 'KILOGRAM'
              }
            }
          ],
          [
            {
              price: {
                value: 2.9,
                unit: 'USD'
              },
              perUnit: {
                unitType: 'WEIGHT',
                unitMeasurement: 'KILOGRAM'
              }
            },
            {
              price: {
                value: 2.7,
                unit: 'USD'
              },
              perUnit: {
                unitType: 'WEIGHT',
                unitMeasurement: 'KILOGRAM'
              }
            },
            {
              price: {
                value: 4.1,
                unit: 'USD'
              },
              perUnit: {
                'unitType': 'WEIGHT',
                'unitMeasurement': 'KILOGRAM'
              }
            }
          ],
          [
            {
              price: {
                value: 2.8,
                unit: 'USD'
              },
              perUnit: {
                unitType: 'WEIGHT',
                unitMeasurement: 'KILOGRAM'
              }
            },
            {
              price: {
                value: 2.6,
                unit: 'USD'
              },
              perUnit: {
                unitType: 'WEIGHT',
                unitMeasurement: 'KILOGRAM'
              }
            },
            {
              price: {
                value: 4.3,
                unit: 'USD'
              },
              perUnit: {
                unitType: 'WEIGHT',
                unitMeasurement: 'KILOGRAM'
              }
            }
          ],
          [
            {
              price: {
                value: 2.3,
                unit: 'USD'
              },
              perUnit: {
                unitType: 'WEIGHT',
                unitMeasurement: 'KILOGRAM'
              }
            },
            {
              price: {
                value: 2,
                unit: 'USD'
              },
              perUnit: {
                unitType: 'WEIGHT',
                unitMeasurement: 'KILOGRAM'
              }
            },
            {
              price: {
                value: 4.1,
                unit: 'USD'
              },
              perUnit: {
                unitType: 'WEIGHT',
                unitMeasurement: 'KILOGRAM'
              }
            }
          ],
          [
            {
              price: {
                value: 1.9,
                unit: 'USD'
              },
              perUnit: {
                unitType: 'WEIGHT',
                unitMeasurement: 'KILOGRAM'
              }
            },
            {
              price: {
                value: 1.7,
                unit: 'USD'
              },
              perUnit: {
                unitType: 'WEIGHT',
                unitMeasurement: 'KILOGRAM'
              }
            },
            {
              price: {
                value: 3.7,
                unit: 'USD'
              },
              perUnit: {
                unitType: 'WEIGHT',
                unitMeasurement: 'KILOGRAM'
              }
            }
          ]
        ]
      },
      {
        commonConditions: [
          {
            type: "ENUM",
            typeCondition: {
              featureId: "featureId",
              optionId: "optionId1"
            },
            numericCondition: null
          },
          {
            type: "NUMERIC",
            typeCondition: null,
            numericCondition: {
              minLimit: 5,
              maxLimit: 100,
              measurementType: "PRICE",
              measurementUnit: "RUB"
            }
          }
        ],
        columns: [
          {
            type: "ENUM",
            typeCondition: {
              featureId: "featureId",
              optionId: "optionId1"
            },
            numericCondition: null
          },
          {
            type: "ENUM",
            typeCondition: {
              featureId: "featureId",
              optionId: "optionId2"
            },
            numericCondition: null
          },
          {
            type: "ENUM",
            typeCondition: {
              featureId: "featureId",
              optionId: "optionId3"
            },
            numericCondition: null
          },
        ],
        rows: [
          {
            type: "NUMERIC",
            typeCondition: null,
            numericCondition: {
              maxLimit: 100,
              measurementType: "WEIGHT",
              measurementUnit: "KILOGRAM"
            }
          },
          {
            type: "NUMERIC",
            typeCondition: null,
            numericCondition: {
              minLimit: 100,
              maxLimit: 200,
              measurementType: "WEIGHT",
              measurementUnit: "KILOGRAM"
            }
          },
          {
            type: "NUMERIC",
            typeCondition: null,
            numericCondition: {
              minLimit: 200,
              maxLimit: 300,
              measurementType: "WEIGHT",
              measurementUnit: "KILOGRAM"
            }
          },
          {
            type: "NUMERIC",
            typeCondition: null,
            numericCondition: {
              minLimit: 300,
              maxLimit: 400,
              measurementType: "WEIGHT",
              measurementUnit: "KILOGRAM"
            }
          },
          {
            type: "NUMERIC",
            typeCondition: null,
            numericCondition: {
              minLimit: 400,
              measurementType: "WEIGHT",
              measurementUnit: "KILOGRAM"
            }
          }
        ],
        tariffPrices: [
          [
            {
              price: {
                value: 3.1,
                unit: 'USD'
              },
              perUnit: {
                unitType: 'WEIGHT',
                unitMeasurement: 'KILOGRAM'
              }
            },
            {
              price: {
                value: 3.3,
                unit: 'USD'
              },
              perUnit: {
                unitType: 'WEIGHT',
                unitMeasurement: 'KILOGRAM'
              }
            },
            {
              price: {
                value: 4.5,
                unit: 'USD'
              },
              perUnit: {
                unitType: 'WEIGHT',
                unitMeasurement: 'KILOGRAM'
              }
            }
          ],
          [
            {
              price: {
                value: 2.9,
                unit: 'USD'
              },
              perUnit: {
                unitType: 'WEIGHT',
                unitMeasurement: 'KILOGRAM'
              }
            },
            {
              price: {
                value: 2.7,
                unit: 'USD'
              },
              perUnit: {
                unitType: 'WEIGHT',
                unitMeasurement: 'KILOGRAM'
              }
            },
            {
              price: {
                value: 4.1,
                unit: 'USD'
              },
              perUnit: {
                'unitType': 'WEIGHT',
                'unitMeasurement': 'KILOGRAM'
              }
            }
          ],
          [
            {
              price: {
                value: 2.8,
                unit: 'USD'
              },
              perUnit: {
                unitType: 'WEIGHT',
                unitMeasurement: 'KILOGRAM'
              }
            },
            {
              price: {
                value: 2.6,
                unit: 'USD'
              },
              perUnit: {
                unitType: 'WEIGHT',
                unitMeasurement: 'KILOGRAM'
              }
            },
            {
              price: {
                value: 4.3,
                unit: 'USD'
              },
              perUnit: {
                unitType: 'WEIGHT',
                unitMeasurement: 'KILOGRAM'
              }
            }
          ],
          [
            {
              price: {
                value: 2.3,
                unit: 'USD'
              },
              perUnit: {
                unitType: 'WEIGHT',
                unitMeasurement: 'KILOGRAM'
              }
            },
            {
              price: {
                value: 2,
                unit: 'USD'
              },
              perUnit: {
                unitType: 'WEIGHT',
                unitMeasurement: 'KILOGRAM'
              }
            },
            {
              price: {
                value: 4.1,
                unit: 'USD'
              },
              perUnit: {
                unitType: 'WEIGHT',
                unitMeasurement: 'KILOGRAM'
              }
            }
          ],
          [
            {
              price: {
                value: 1.9,
                unit: 'USD'
              },
              perUnit: {
                unitType: 'WEIGHT',
                unitMeasurement: 'KILOGRAM'
              }
            },
            {
              price: {
                value: 1.7,
                unit: 'USD'
              },
              perUnit: {
                unitType: 'WEIGHT',
                unitMeasurement: 'KILOGRAM'
              }
            },
            {
              price: {
                value: 3.7,
                unit: 'USD'
              },
              perUnit: {
                unitType: 'WEIGHT',
                unitMeasurement: 'KILOGRAM'
              }
            }
          ]
        ]
      },
      {
        commonConditions: [
          {
            type: "ENUM",
            typeCondition: {
              featureId: "featureId",
              optionId: "optionId1"
            },
            numericCondition: null
          },
          {
            type: "NUMERIC",
            typeCondition: null,
            numericCondition: {
              minLimit: 5,
              maxLimit: 100,
              measurementType: "PRICE",
              measurementUnit: "RUB"
            }
          }
        ],
        columns: [
          {
            type: "ENUM",
            typeCondition: {
              featureId: "featureId",
              optionId: "optionId1"
            },
            numericCondition: null
          },
          {
            type: "ENUM",
            typeCondition: {
              featureId: "featureId",
              optionId: "optionId2"
            },
            numericCondition: null
          },
          {
            type: "ENUM",
            typeCondition: {
              featureId: "featureId",
              optionId: "optionId3"
            },
            numericCondition: null
          },
        ],
        rows: [
          {
            type: "NUMERIC",
            typeCondition: null,
            numericCondition: {
              maxLimit: 100,
              measurementType: "WEIGHT",
              measurementUnit: "KILOGRAM"
            }
          },
          {
            type: "NUMERIC",
            typeCondition: null,
            numericCondition: {
              minLimit: 100,
              maxLimit: 200,
              measurementType: "WEIGHT",
              measurementUnit: "KILOGRAM"
            }
          },
          {
            type: "NUMERIC",
            typeCondition: null,
            numericCondition: {
              minLimit: 200,
              maxLimit: 300,
              measurementType: "WEIGHT",
              measurementUnit: "KILOGRAM"
            }
          },
          {
            type: "NUMERIC",
            typeCondition: null,
            numericCondition: {
              minLimit: 300,
              maxLimit: 400,
              measurementType: "WEIGHT",
              measurementUnit: "KILOGRAM"
            }
          },
          {
            type: "NUMERIC",
            typeCondition: null,
            numericCondition: {
              minLimit: 400,
              measurementType: "WEIGHT",
              measurementUnit: "KILOGRAM"
            }
          }
        ],
        tariffPrices: [
          [
            {
              price: {
                value: 3.1,
                unit: 'USD'
              },
              perUnit: {
                unitType: 'WEIGHT',
                unitMeasurement: 'KILOGRAM'
              }
            },
            {
              price: {
                value: 3.3,
                unit: 'USD'
              },
              perUnit: {
                unitType: 'WEIGHT',
                unitMeasurement: 'KILOGRAM'
              }
            },
            {
              price: {
                value: 4.5,
                unit: 'USD'
              },
              perUnit: {
                unitType: 'WEIGHT',
                unitMeasurement: 'KILOGRAM'
              }
            }
          ],
          [
            {
              price: {
                value: 2.9,
                unit: 'USD'
              },
              perUnit: {
                unitType: 'WEIGHT',
                unitMeasurement: 'KILOGRAM'
              }
            },
            {
              price: {
                value: 2.7,
                unit: 'USD'
              },
              perUnit: {
                unitType: 'WEIGHT',
                unitMeasurement: 'KILOGRAM'
              }
            },
            {
              price: {
                value: 4.1,
                unit: 'USD'
              },
              perUnit: {
                'unitType': 'WEIGHT',
                'unitMeasurement': 'KILOGRAM'
              }
            }
          ],
          [
            {
              price: {
                value: 2.8,
                unit: 'USD'
              },
              perUnit: {
                unitType: 'WEIGHT',
                unitMeasurement: 'KILOGRAM'
              }
            },
            {
              price: {
                value: 2.6,
                unit: 'USD'
              },
              perUnit: {
                unitType: 'WEIGHT',
                unitMeasurement: 'KILOGRAM'
              }
            },
            {
              price: {
                value: 4.3,
                unit: 'USD'
              },
              perUnit: {
                unitType: 'WEIGHT',
                unitMeasurement: 'KILOGRAM'
              }
            }
          ],
          [
            {
              price: {
                value: 2.3,
                unit: 'USD'
              },
              perUnit: {
                unitType: 'WEIGHT',
                unitMeasurement: 'KILOGRAM'
              }
            },
            {
              price: {
                value: 2,
                unit: 'USD'
              },
              perUnit: {
                unitType: 'WEIGHT',
                unitMeasurement: 'KILOGRAM'
              }
            },
            {
              price: {
                value: 4.1,
                unit: 'USD'
              },
              perUnit: {
                unitType: 'WEIGHT',
                unitMeasurement: 'KILOGRAM'
              }
            }
          ],
          [
            {
              price: {
                value: 1.9,
                unit: 'USD'
              },
              perUnit: {
                unitType: 'WEIGHT',
                unitMeasurement: 'KILOGRAM'
              }
            },
            {
              price: {
                value: 1.7,
                unit: 'USD'
              },
              perUnit: {
                unitType: 'WEIGHT',
                unitMeasurement: 'KILOGRAM'
              }
            },
            {
              price: {
                value: 3.7,
                unit: 'USD'
              },
              perUnit: {
                unitType: 'WEIGHT',
                unitMeasurement: 'KILOGRAM'
              }
            }
          ]
        ]
      }
    ]
  },
  {
    tariffId: "tariffId2",
    title: 'Упаковка',
    description: 'Упаковка посылок для безопасности бла бла бла',
    applyLevel: 'PRODUCT',
    features: [
      {
        id: "featureId",
        title: "Тип",
        description: "",
        options: [
          {
            id: "optionId1",
            title: "Пакет",
            description: "Не включает обувь, аксессуары"
          },
          {
            id: "optionId2",
            title: "Коробка",
            description: "Утварь для дома"
          },
          {
            id: "optionId3",
            title: "Палет",
            description: "Компьютеры, гаджеты и т.д."
          },
        ]
      }
    ],
    tariffTables: [
      {
        columns: [
          { 
            type: 'ENUM',
            typeCondition: {
              featureId: 'featureId',
              optionId: 'optionId1'
            }
          },
          { 
            type: 'ENUM',
            typeCondition: {
              featureId: 'featureId',
              optionId: 'optionId2'
            }
          },
          { 
            type: 'ENUM',
            typeCondition: {
              featureId: 'featureId',
              optionId: 'optionId3'
            }
          }
        ],
        tariffPrices: [
            [
              {
                price: {
                  value: 7,
                  unit: 'USD'
                },
                perUnit: {
                  unitType: 'PRODUCT_NUM'
                }
              },
              {
                price: {
                  value: 8,
                  unit: 'USD'
                },
                perUnit: {
                  unitType: 'PRODUCT_NUM'
                }
              },
              {
                price: {
                  value: 9,
                  unit: 'USD'
                },
                perUnit: {
                  unitType: 'PRODUCT_NUM'
                }
              }
            ]
        ]
      }
    ]
  },
  {
    tariffId: 'tariffId3',
    title: 'Выкуп товаров',
    tariffTables: [
      {
        tariffPrices: [
          [
            {
              price: {
                value: 9,
              },
              perUnit: {
                unitType: 'PERCENTAGE'
              }
            }
          ]
        ]
      }
    ]
  }
]

export default function AgentPublicProfile() {
  const agent = useCurrentAgent();

  return <Stack spacing={2}>
    {agent && <AgentInfo agent={agent.agent}/>}
    {agent?.tariffs && <Tariffs tariffs={agent.tariffs}/>}
  </Stack>
};