package ru.hse.fcs.tariff.service.interfaces.tariff.dto.converter

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.hse.fcs.tariff.service.domain.measure.unit.*
import ru.hse.fcs.tariff.service.domain.tariff.*
import ru.hse.fcs.tariff.service.interfaces.tariff.dto.*

@Component
class TariffToTariffDtoConverter : Converter<Tariff, TariffDto> {

    override fun convert(source: Tariff): TariffDto {
        return TariffDto(
            tariffId = source.tariffId,
            agentId = source.agentId,
            applyLevel = when (source.applyLevel) {
                Tariff.ApplyLevel.ORDER -> TariffDto.ApplyLevel.ORDER
                Tariff.ApplyLevel.PRODUCT -> TariffDto.ApplyLevel.PRODUCT
            },
            title = source.title,
            description = source.description,
            features = source.features.map(::convert),
            tariffTables = convert(source.tariffItems)
        )
    }

    private fun convert(feature: TariffFeature): FeatureDto {
        return FeatureDto(
            id = feature.featureId,
            title = feature.title,
            description = feature.description,
            options = feature.options.map(::convert)
        )
    }

    private fun convert(option: TariffFeatureOption): FeatureOptionDto {
        return FeatureOptionDto(
            id = option.optionId,
            title = option.title,
            description = option.description
        )
    }

    private fun convert(tariffItems: List<TariffItem>): List<TariffTableDto> {
        val itemsGroupedForTables = tariffItems.groupBy { item ->
            item.conditions.subList(0, item.conditions.size - 2)
        }

        val tables = itemsGroupedForTables.entries.map { (commonConditions, tableTariffItems) ->
            val rowConditions = mutableListOf<TariffCondition>()
            val columnConditions = mutableListOf<TariffCondition>()
            val table = mutableListOf<MutableList<TariffPriceDto?>>()

            tableTariffItems.map {
                val rowCondition = it.conditions.last()
                val columnCondition = it.conditions[it.conditions.size - 2]

                var rowIndex = rowConditions.indexOf(rowCondition)
                if (rowIndex == -1) {
                    rowIndex = rowConditions.size
                    rowConditions.add(rowCondition)
                    table.add(
                        arrayOfNulls<TariffPriceDto>(columnConditions.size).toMutableList()
                    )
                }

                var columnIndex = columnConditions.indexOf(columnCondition)
                if (columnIndex == -1) {
                    columnIndex = columnConditions.size
                    columnConditions.add(columnCondition)
                    table.forEach { row -> row.add(null) }
                }

                val priceDto = convert(it.pricePerUnit)
                table[rowIndex][columnIndex] = priceDto
            }

            TariffTableDto(
                commonConditions = commonConditions.map(::convert),
                rows = rowConditions.map(::convert),
                columns = columnConditions.map(::convert),
                tariffPrices = table
            )
        }

        return tables
    }

    private fun convert(condition: TariffCondition): TariffConditionDto {
        return when (condition) {
            is TariffNumericCondition ->
                TariffConditionDto(
                    type = TariffConditionDto.TariffConditionType.NUMERIC,
                    numericCondition = TariffNumericConditionDto(
                        minLimit = condition.minLimit,
                        maxLimit = condition.maxLimit,
                        measurementType = when (condition.measurementUnit.type) {
                            TariffNumericCondition.NumericConditionMeasurement.Type.WEIGHT ->
                                TariffNumericConditionDto.ConditionMeasurementTypeDto.WEIGHT
                            TariffNumericCondition.NumericConditionMeasurement.Type.VOLUME ->
                                TariffNumericConditionDto.ConditionMeasurementTypeDto.VOLUME
                            TariffNumericCondition.NumericConditionMeasurement.Type.DENSITY ->
                                TariffNumericConditionDto.ConditionMeasurementTypeDto.DENSITY
                            TariffNumericCondition.NumericConditionMeasurement.Type.UNITS ->
                                TariffNumericConditionDto.ConditionMeasurementTypeDto.UNITS
                            TariffNumericCondition.NumericConditionMeasurement.Type.PRICE ->
                                TariffNumericConditionDto.ConditionMeasurementTypeDto.PRICE
                        },
                        measurementUnit = when (condition.measurementUnit.type) {
                            TariffNumericCondition.NumericConditionMeasurement.Type.WEIGHT ->
                                when (condition.measurementUnit.unit as WeightUnit) {
                                   WeightUnit.KILOGRAM -> MeasurementUnitDto.KILOGRAM
                                   WeightUnit.GRAM -> MeasurementUnitDto.GRAM
                                   WeightUnit.TON -> MeasurementUnitDto.TON
                                   WeightUnit.POUND -> MeasurementUnitDto.POUND
                                   WeightUnit.OUNCE -> MeasurementUnitDto.OUNCE
                                }

                            TariffNumericCondition.NumericConditionMeasurement.Type.VOLUME ->
                                when (condition.measurementUnit.unit as VolumeUnit) {
                                    VolumeUnit.CUBIC_METER -> MeasurementUnitDto.CUBIC_METER
                                    VolumeUnit.LITER -> MeasurementUnitDto.LITER
                                    VolumeUnit.MILLILITER -> MeasurementUnitDto.MILLILITER
                                    VolumeUnit.GALLON -> MeasurementUnitDto.GALLON
                                    VolumeUnit.QUART -> MeasurementUnitDto.QUART
                                    VolumeUnit.CUBIC_FOOT -> MeasurementUnitDto.CUBIC_FOOT
                                }

                            TariffNumericCondition.NumericConditionMeasurement.Type.DENSITY ->
                                when (condition.measurementUnit.unit as DensityUnit) {
                                    DensityUnit.KG_PER_CUBIC_METER -> MeasurementUnitDto.KG_PER_CUBIC_CM
                                    DensityUnit.GRAM_PER_CUBIC_CM -> MeasurementUnitDto.GRAM_PER_CUBIC_CM
                                    DensityUnit.KG_PER_CUBIC_CM -> MeasurementUnitDto.KG_PER_CUBIC_CM
                                    DensityUnit.GRAM_PER_CUBIC_METER -> MeasurementUnitDto.GRAM_PER_CUBIC_METER
                                    DensityUnit.POUNDS_PER_CUBIC_FOOT -> MeasurementUnitDto.POUNDS_PER_CUBIC_FOOT

                                }

                            TariffNumericCondition.NumericConditionMeasurement.Type.PRICE ->
                                when ((condition.measurementUnit.unit as CurrencyUnit).currency) {
                                    Currency.RUB -> MeasurementUnitDto.RUB
                                    Currency.EUR -> MeasurementUnitDto.EUR
                                    Currency.CNY -> MeasurementUnitDto.CNY
                                    Currency.USD -> MeasurementUnitDto.USD
                                }

                            TariffNumericCondition.NumericConditionMeasurement.Type.UNITS -> null
                        }
                    )
                )

            is TariffTypeCondition ->
                TariffConditionDto(
                    type = TariffConditionDto.TariffConditionType.ENUM,
                    typeCondition = TariffTypeConditionDto(
                        feature = condition.typeId,
                        option = condition.optionId
                    )
                )
        }
    }

    private fun convert(pricePerUnit: PricePerUnit): TariffPriceDto {
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