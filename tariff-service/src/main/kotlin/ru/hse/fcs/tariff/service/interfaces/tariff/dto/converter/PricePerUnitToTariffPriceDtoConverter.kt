package ru.hse.fcs.tariff.service.interfaces.tariff.dto.converter

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.hse.fcs.tariff.service.domain.measure.unit.Currency
import ru.hse.fcs.tariff.service.domain.measure.unit.DensityUnit
import ru.hse.fcs.tariff.service.domain.measure.unit.VolumeUnit
import ru.hse.fcs.tariff.service.domain.measure.unit.WeightUnit
import ru.hse.fcs.tariff.service.domain.tariff.PricePerUnit
import ru.hse.fcs.tariff.service.interfaces.tariff.dto.MeasurementUnitDto
import ru.hse.fcs.tariff.service.interfaces.tariff.dto.PriceDto
import ru.hse.fcs.tariff.service.interfaces.tariff.dto.TariffPriceDto

@Component
class PricePerUnitToTariffPriceDtoConverter: Converter<PricePerUnit, TariffPriceDto> {

    override fun convert(pricePerUnit: PricePerUnit): TariffPriceDto {
        return TariffPriceDto(
            price = PriceDto(
                value = pricePerUnit.price.value,
                unit = when (pricePerUnit.price.unit.currency) {
                    Currency.RUB -> PriceDto.CurrencyDto.RUB
                    Currency.EUR -> PriceDto.CurrencyDto.EUR
                    Currency.CNY -> PriceDto.CurrencyDto.CNY
                    Currency.USD -> PriceDto.CurrencyDto.USD
                }
            ),
            perUnit = TariffPriceDto.PerUnitDto(
                unitType = when (pricePerUnit.unit.type) {
                    PricePerUnit.Unit.UnitType.PRODUCT_NUM -> TariffPriceDto.PerUnitDto.UnitType.PRODUCT_NUM
                    PricePerUnit.Unit.UnitType.PRODUCT_TYPE_UNIT -> TariffPriceDto.PerUnitDto.UnitType.PRODUCT_TYPE_UNIT
                    PricePerUnit.Unit.UnitType.WEIGHT -> TariffPriceDto.PerUnitDto.UnitType.WEIGHT
                    PricePerUnit.Unit.UnitType.VOLUME -> TariffPriceDto.PerUnitDto.UnitType.VOLUME
                    PricePerUnit.Unit.UnitType.DENSITY -> TariffPriceDto.PerUnitDto.UnitType.DENSITY
                    PricePerUnit.Unit.UnitType.FIXED -> TariffPriceDto.PerUnitDto.UnitType.FIXED
                    PricePerUnit.Unit.UnitType.PERCENTAGE -> TariffPriceDto.PerUnitDto.UnitType.PERCENTAGE
                },
                unit = when (pricePerUnit.unit.type) {
                    PricePerUnit.Unit.UnitType.WEIGHT ->
                        when (pricePerUnit.unit.unit as WeightUnit) {
                            WeightUnit.KILOGRAM -> MeasurementUnitDto.KILOGRAM
                            WeightUnit.GRAM -> MeasurementUnitDto.GRAM
                            WeightUnit.TON -> MeasurementUnitDto.TON
                            WeightUnit.POUND -> MeasurementUnitDto.POUND
                            WeightUnit.OUNCE -> MeasurementUnitDto.OUNCE
                        }

                    PricePerUnit.Unit.UnitType.VOLUME ->
                        when (pricePerUnit.unit.unit as VolumeUnit) {
                            VolumeUnit.CUBIC_METER -> MeasurementUnitDto.CUBIC_METER
                            VolumeUnit.LITER -> MeasurementUnitDto.LITER
                            VolumeUnit.MILLILITER -> MeasurementUnitDto.MILLILITER
                            VolumeUnit.GALLON -> MeasurementUnitDto.GALLON
                            VolumeUnit.QUART -> MeasurementUnitDto.QUART
                            VolumeUnit.CUBIC_FOOT -> MeasurementUnitDto.CUBIC_FOOT
                        }

                    PricePerUnit.Unit.UnitType.DENSITY ->
                        when (pricePerUnit.unit.unit as DensityUnit) {
                            DensityUnit.KG_PER_CUBIC_METER -> MeasurementUnitDto.KG_PER_CUBIC_CM
                            DensityUnit.GRAM_PER_CUBIC_CM -> MeasurementUnitDto.GRAM_PER_CUBIC_CM
                            DensityUnit.KG_PER_CUBIC_CM -> MeasurementUnitDto.KG_PER_CUBIC_CM
                            DensityUnit.GRAM_PER_CUBIC_METER -> MeasurementUnitDto.GRAM_PER_CUBIC_METER
                            DensityUnit.POUNDS_PER_CUBIC_FOOT -> MeasurementUnitDto.POUNDS_PER_CUBIC_FOOT

                        }

                    else -> null
                }
            )
        )
    }
}
