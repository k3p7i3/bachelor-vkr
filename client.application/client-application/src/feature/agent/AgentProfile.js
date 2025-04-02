import { useParams } from 'react-router-dom';
import PageStackContainer from '../../component/ui/PageStackContainer';
import AgentInfo from './AgentInfo';
import TariffTable from './tariff/TariffTable';
import CenteredMediumCard from '../../component/ui/CenteredMediumCard';
import { Stack, Typography } from '@mui/material';
import CustomCarousel from '../../component/ui/CustomCarousel';
import Tariffs from './tariff/TariffInfo';

// agent info
export const agentInfo = {
  name: "Иван Ивановов",
  contactPhone: "+79971234567",
  contactEmail: "agent@gmail.com",
  description: `Привет! Я занимаюсь грузоперевозками из Китая в Россию уже более 10 лет. Вот некоторая информация обо мне:
Я специализируюсь на организации международных грузоперевозок из Китая в Россию.
За годы своей деятельности я установил прочные связи и сотрудничество с логистическими компаниями, перевозчиками и таможенными службами, что позволяет мне оказывать качественные и надежные услуги.
Я предлагаю широкий спектр услуг, включая организацию перевозки грузов различными видами транспорта, таможенное оформление, сопровождение груза на всех этапах доставки и многое другое.
Моя команда и я готовы предложить индивидуальные решения и подходы к каждому клиенту, учитывая их потребности и требования.
Мои клиенты ценят мою ответственность, профессионализм и эффективность в выполнении задач.
Если у вас возникли вопросы или вам требуется помощь с грузоперевозками из Китая в Россию, я всегда готов помочь`,

  avatarUrl: "https://img.freepik.com/free-photo/smiley-man-relaxing-outdoors_23-2148739334.jpg",
  
  images: [
    "https://fastimport.ru/wp-content/uploads/2019/03/deshevye-smartfony-iz-kitaya.jpg",
    "https://logirus.ru/upload/iblock/e1c/36lddynvhmci2g74zh527v5j31z5iqk9/ki_p_30_12.jpg",
    "https://chin-ru.com/wp-content/uploads/2021/02/kargo1.jpg",
    "https://fastimport.ru/wp-content/uploads/2019/03/deshevye-smartfony-iz-kitaya.jpg",
    "https://logirus.ru/upload/iblock/e1c/36lddynvhmci2g74zh527v5j31z5iqk9/ki_p_30_12.jpg",
    "https://chin-ru.com/wp-content/uploads/2021/02/kargo1.jpg",
    "https://fastimport.ru/wp-content/uploads/2019/03/deshevye-smartfony-iz-kitaya.jpg",
    "https://logirus.ru/upload/iblock/e1c/36lddynvhmci2g74zh527v5j31z5iqk9/ki_p_30_12.jpg",
    "https://chin-ru.com/wp-content/uploads/2021/02/kargo1.jpg"
  ],

  rating: 4.5,
  reviewsNumber: 34
}


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

export default function AgentProfile() {

  const params = useParams();
  const agentId = params.agentId;

  return <PageStackContainer>

    <Stack spacing={2}>
      <AgentInfo agent={agentInfo}/>
      <Tariffs tariffs={tariffs}/>
    </Stack>
    
  </PageStackContainer>
};