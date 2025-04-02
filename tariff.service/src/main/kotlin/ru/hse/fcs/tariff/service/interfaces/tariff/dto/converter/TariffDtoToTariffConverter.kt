package ru.hse.fcs.tariff.service.interfaces.tariff.dto.converter

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.hse.fcs.tariff.service.domain.measure.Price
import ru.hse.fcs.tariff.service.domain.measure.unit.*
import ru.hse.fcs.tariff.service.domain.tariff.*
import ru.hse.fcs.tariff.service.interfaces.tariff.dto.*
import java.util.UUID

@Component
class TariffDtoToTariffConverter(
    private val currencyUnitFactory: CurrencyUnitFactory
) : Converter<TariffDto, Tariff> {

    override fun convert(source: TariffDto): Tariff {
        return Tariff(
            tariffId = source.tariffId,
            agentId = source.agentId,
            applyLevel = when (source.applyLevel) {
                TariffDto.ApplyLevel.ORDER -> Tariff.ApplyLevel.ORDER
                TariffDto.ApplyLevel.PRODUCT -> Tariff.ApplyLevel.PRODUCT
            },
            title = source.title,
            description = source.description,
            features = source.features.map(::convert),
            tariffItems = source.tariffTables.flatMap { tariffTable ->
                convert(source.agentId, tariffTable)
            }
        )
    }

    private fun convert(feature: FeatureDto): TariffFeature {
        return TariffFeature(
            featureId = feature.id ?: UUID.randomUUID(),
            title = feature.title,
            description = feature.description,
            options = feature.options.map(::convert)
        )
    }

    private fun convert(option: FeatureOptionDto): TariffFeatureOption {
        return TariffFeatureOption(
            optionId = option.id ?: UUID.randomUUID(),
            title = option.title,
            description = option.description
        )
    }

    private fun convert(agentId: UUID, tariffTable: TariffTableDto): List<TariffItem> {
        val commonConditions = tariffTable.commonConditions.map { convert(agentId, it) }

        return tariffTable.tariffPrices.flatMapIndexed { rowIndex, tariffPrices ->
            val rowCondition = convert(agentId, tariffTable.rows[rowIndex])

            tariffPrices.mapIndexedNotNull { columnIndex, tariffPrice ->
                val columnCondition = convert(agentId, tariffTable.columns[columnIndex])

                tariffPrice?.let { tariffPrice ->
                    val pricePerUnit = convert(agentId, tariffPrice)

                    TariffItem(
                        tariffItemId = UUID.randomUUID(), // null
                        pricePerUnit = pricePerUnit,
                        conditions = commonConditions + listOf(columnCondition, rowCondition)
                    )
                }
            }
        }
    }

    private fun convert(agentId: UUID, condition: TariffConditionDto): TariffCondition {
        return when (condition.type) {
            TariffConditionDto.TariffConditionType.ENUM ->
                TariffTypeCondition(
                    typeId = condition.typeCondition!!.feature,
                    optionId = condition.typeCondition.option
                )
            TariffConditionDto.TariffConditionType.NUMERIC ->
                TariffNumericCondition(
                    minLimit = condition.numericCondition!!.minLimit,
                    maxLimit = condition.numericCondition.maxLimit,
                    measurementUnit = convert(
                        agentId = agentId,
                        measurementType = condition.numericCondition.measurementType,
                        unit = condition.numericCondition.measurementUnit
                    )
                )
        }
    }

    private fun convert(
        agentId: UUID,
        measurementType: TariffNumericConditionDto.ConditionMeasurementTypeDto,
        unit: MeasurementUnitDto?
    ): TariffNumericCondition.NumericConditionMeasurement {
        return when (measurementType) {
            TariffNumericConditionDto.ConditionMeasurementTypeDto.UNITS ->
                TariffNumericCondition.NumericConditionMeasurement(
                    type = TariffNumericCondition.NumericConditionMeasurement.Type.UNITS,
                    unit = null
                )

            TariffNumericConditionDto.ConditionMeasurementTypeDto.PRICE -> {
                val currency: Currency = when (unit) {
                    MeasurementUnitDto.RUB -> Currency.RUB
                    MeasurementUnitDto.EUR -> Currency.EUR
                    MeasurementUnitDto.CNY -> Currency.CNY
                    MeasurementUnitDto.USD -> Currency.USD
                    else -> throw IllegalArgumentException("Wrong currency argument")
                }

                TariffNumericCondition.NumericConditionMeasurement(
                    type = TariffNumericCondition.NumericConditionMeasurement.Type.PRICE,
                    unit = currencyUnitFactory.buildCurrencyUnit(agentId, currency)
                )
            }

            TariffNumericConditionDto.ConditionMeasurementTypeDto.WEIGHT ->
                TariffNumericCondition.NumericConditionMeasurement(
                    type = TariffNumericCondition.NumericConditionMeasurement.Type.WEIGHT,
                    unit = when (unit) {
                        MeasurementUnitDto.KILOGRAM -> WeightUnit.KILOGRAM
                        MeasurementUnitDto.GRAM -> WeightUnit.GRAM
                        MeasurementUnitDto.TON -> WeightUnit.TON
                        MeasurementUnitDto.POUND -> WeightUnit.POUND
                        MeasurementUnitDto.OUNCE -> WeightUnit.OUNCE
                        else -> throw IllegalArgumentException("Wrong weight unit")
                    }
                )

            TariffNumericConditionDto.ConditionMeasurementTypeDto.VOLUME ->
                TariffNumericCondition.NumericConditionMeasurement(
                    type = TariffNumericCondition.NumericConditionMeasurement.Type.VOLUME,
                    unit = when (unit) {
                        MeasurementUnitDto.CUBIC_METER -> VolumeUnit.CUBIC_METER
                        MeasurementUnitDto.LITER -> VolumeUnit.LITER
                        MeasurementUnitDto.MILLILITER -> VolumeUnit.MILLILITER
                        MeasurementUnitDto.GALLON -> VolumeUnit.GALLON
                        MeasurementUnitDto.QUART -> VolumeUnit.QUART
                        MeasurementUnitDto.CUBIC_FOOT -> VolumeUnit.CUBIC_FOOT
                        else -> throw IllegalArgumentException("Wrong volume unit")
                    }
                )

            TariffNumericConditionDto.ConditionMeasurementTypeDto.DENSITY ->
                TariffNumericCondition.NumericConditionMeasurement(
                    type = TariffNumericCondition.NumericConditionMeasurement.Type.DENSITY,
                    unit = when (unit) {
                        MeasurementUnitDto.KG_PER_CUBIC_METER -> DensityUnit.KG_PER_CUBIC_CM
                        MeasurementUnitDto.GRAM_PER_CUBIC_CM -> DensityUnit.GRAM_PER_CUBIC_CM
                        MeasurementUnitDto.KG_PER_CUBIC_CM -> DensityUnit.KG_PER_CUBIC_CM
                        MeasurementUnitDto.GRAM_PER_CUBIC_METER -> DensityUnit.GRAM_PER_CUBIC_METER
                        MeasurementUnitDto.POUNDS_PER_CUBIC_FOOT -> DensityUnit.POUNDS_PER_CUBIC_FOOT
                        else -> throw IllegalArgumentException("Wrong density unit")
                    }
                )
        }
    }

    private fun convert(agentId: UUID, tariffPrice: TariffPriceDto): PricePerUnit {
        return PricePerUnit(
            price = Price(
                value = tariffPrice.price.value,
                unit = currencyUnitFactory.buildCurrencyUnit(
                    agentId,
                    currency = when (tariffPrice.price.unit) {
                        PriceDto.CurrencyDto.CNY -> Currency.CNY
                        PriceDto.CurrencyDto.EUR -> Currency.EUR
                        PriceDto.CurrencyDto.RUB -> Currency.RUB
                        PriceDto.CurrencyDto.USD -> Currency.USD
                    }
                )
            ),
            unit = when (tariffPrice.perUnit.unitType) {
                TariffPriceDto.PerUnitDto.UnitType.PRODUCT_NUM ->
                    PricePerUnit.Unit(
                        type = PricePerUnit.Unit.UnitType.PRODUCT_NUM,
                        unit = null
                    )

                TariffPriceDto.PerUnitDto.UnitType.PRODUCT_TYPE_UNIT ->
                    PricePerUnit.Unit(
                        type = PricePerUnit.Unit.UnitType.PRODUCT_TYPE_UNIT,
                        unit = null
                    )

                TariffPriceDto.PerUnitDto.UnitType.WEIGHT ->
                    PricePerUnit.Unit(
                        type = PricePerUnit.Unit.UnitType.WEIGHT,
                        unit = when (tariffPrice.perUnit.unit) {
                            MeasurementUnitDto.KILOGRAM -> WeightUnit.KILOGRAM
                            MeasurementUnitDto.GRAM -> WeightUnit.GRAM
                            MeasurementUnitDto.TON -> WeightUnit.TON
                            MeasurementUnitDto.POUND -> WeightUnit.POUND
                            MeasurementUnitDto.OUNCE -> WeightUnit.OUNCE
                            else -> throw IllegalArgumentException("Wrong weight unit")
                        }
                    )

                TariffPriceDto.PerUnitDto.UnitType.VOLUME ->
                    PricePerUnit.Unit(
                        type = PricePerUnit.Unit.UnitType.VOLUME,
                        unit = when (tariffPrice.perUnit.unit) {
                            MeasurementUnitDto.CUBIC_METER -> VolumeUnit.CUBIC_METER
                            MeasurementUnitDto.LITER -> VolumeUnit.LITER
                            MeasurementUnitDto.MILLILITER -> VolumeUnit.MILLILITER
                            MeasurementUnitDto.GALLON -> VolumeUnit.GALLON
                            MeasurementUnitDto.QUART -> VolumeUnit.QUART
                            MeasurementUnitDto.CUBIC_FOOT -> VolumeUnit.CUBIC_FOOT
                            else -> throw IllegalArgumentException("Wrong volume unit")
                        }
                    )

                TariffPriceDto.PerUnitDto.UnitType.DENSITY ->
                    PricePerUnit.Unit(
                        type = PricePerUnit.Unit.UnitType.DENSITY,
                        unit = when (tariffPrice.perUnit.unit) {
                            MeasurementUnitDto.KG_PER_CUBIC_METER -> DensityUnit.KG_PER_CUBIC_CM
                            MeasurementUnitDto.GRAM_PER_CUBIC_CM -> DensityUnit.GRAM_PER_CUBIC_CM
                            MeasurementUnitDto.KG_PER_CUBIC_CM -> DensityUnit.KG_PER_CUBIC_CM
                            MeasurementUnitDto.GRAM_PER_CUBIC_METER -> DensityUnit.GRAM_PER_CUBIC_METER
                            MeasurementUnitDto.POUNDS_PER_CUBIC_FOOT -> DensityUnit.POUNDS_PER_CUBIC_FOOT
                            else -> throw IllegalArgumentException("Wrong density unit")
                        }
                    )

                TariffPriceDto.PerUnitDto.UnitType.FIXED ->
                    PricePerUnit.Unit(
                        type = PricePerUnit.Unit.UnitType.FIXED,
                        unit = null
                    )

                TariffPriceDto.PerUnitDto.UnitType.PERCENTAGE ->
                    PricePerUnit.Unit(
                        type = PricePerUnit.Unit.UnitType.PERCENTAGE,
                        unit = null
                    )
            }
        )
    }
}