import { Typography } from "@mui/material"
import { mapUnitMeasurement, mapPerUnitType } from "./TariffMapUtils"

// data class TariffPriceDto(
//   val price: PriceDto,
//   val perUnit: PerUnitDto
// ) {
//   data class PerUnitDto(
//       val unitType: UnitType,
//       val unit: MeasurementUnitDto?
//   ) {
//       enum class UnitType {
//           PRODUCT_NUM, PRODUCT_TYPE_UNIT, WEIGHT, VOLUME, DENSITY, FIXED, PERCENTAGE
//       }
//   }
// }

// data class PriceDto(
//   val value: BigDecimal,
//   val unit: CurrencyDto
// ) {
//   enum class CurrencyDto { RUB, CNY, USD, EUR }
// }

export function TariffPriceCell({ price }) {
  const priceText = price.price.value + (price.price.unit ? mapUnitMeasurement[price.price.unit] : '')
  const perUnit = (mapPerUnitType[price.perUnit.unitType] + 
    (price.perUnit.unitMeasurement ? mapUnitMeasurement[price.perUnit.unitMeasurement] : '')
  )

  return <Typography variant="body1">{priceText}{perUnit}</Typography>
};