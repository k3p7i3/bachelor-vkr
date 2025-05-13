package ru.hse.fcs.tariff.service.interfaces.tariff.dto.converter

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertInstanceOf
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever
import ru.hse.fcs.tariff.service.domain.measure.Price
import ru.hse.fcs.tariff.service.domain.measure.unit.*
import ru.hse.fcs.tariff.service.domain.tariff.*
import ru.hse.fcs.tariff.service.interfaces.tariff.dto.*
import java.math.BigDecimal
import java.util.UUID
import kotlin.test.assertEquals

internal class ConverterTest {

    @Test
    fun testConvertToDto() {
        val converter = TariffToTariffDtoConverter()
        val resultTariff = converter.convert(tariff)
        assertEquals(tariffDto, resultTariff)
    }

    @Test
    fun testConvertFromDto() {
        val converter = TariffDtoToTariffConverter()
        val resultTariff = converter.convert(tariffDto)

        assertEquals(4, resultTariff.tariffItems.size)
        resultTariff.tariffItems.forEachIndexed { idx, tariffItem ->

            tariffItem.conditions.forEachIndexed { index, tariffCondition ->
                if (index == tariffItem.conditions.size - 1) {
                    assertInstanceOf<TariffTypeCondition>(tariffCondition)
                    assertEquals(feature.featureId, tariffCondition.typeId)
                    assertEquals(feature.options[idx % 2].optionId, tariffCondition.optionId)
                } else {
                    assertInstanceOf<TariffNumericCondition>(tariffCondition)
                    val expectedCondition = tariff.tariffItems[idx].conditions[index] as TariffNumericCondition
                    assertEquals(expectedCondition.maxLimit, tariffCondition.maxLimit)
                    assertEquals(expectedCondition.minLimit, tariffCondition.minLimit)
                    assertEquals(expectedCondition.measurementUnit.type, tariffCondition.measurementUnit.type)
                    assertEquals(expectedCondition.measurementUnit.unit, tariffCondition.measurementUnit.unit)
                }
            }

            val expectedPrice = tariff.tariffItems[idx].pricePerUnit
            assertEquals(expectedPrice.price.value, tariffItem.pricePerUnit.price.value)
            assertEquals(expectedPrice.price.unit, tariffItem.pricePerUnit.price.unit)
            assertEquals(expectedPrice.unit.type, tariffItem.pricePerUnit.unit.type)
            assertEquals(expectedPrice.unit.unit, tariffItem.pricePerUnit.unit.unit)
        }
    }

    private val tariffId = UUID.fromString("e74773e8-2619-4c2c-8064-8c253ada71b6")
    private val agentId = UUID.fromString("7d329028-ff2a-4a78-859e-72659afad4ee")

    private val feature = TariffFeature(
        featureId = UUID.randomUUID(),
        title = "Category",
        description = "All products are from one category",
        options = listOf(
            TariffFeatureOption(
                optionId = UUID.randomUUID(),
                title = "Clothes",
                description = "Dresses, shorts, shirts, etc"
            ),
            TariffFeatureOption(
                optionId = UUID.randomUUID(),
                title = "Home products",
                description = "Stuff for home"
            )
        )
    )

    private val commonConditions = listOf(
        TariffNumericCondition(
            measurementUnit = TariffNumericCondition.NumericConditionMeasurement(
                type = TariffNumericCondition.NumericConditionMeasurement.Type.UNITS
            ),
            maxLimit = "10000".toBigDecimal()
        ),
        TariffNumericCondition(
            measurementUnit = TariffNumericCondition.NumericConditionMeasurement(
                type = TariffNumericCondition.NumericConditionMeasurement.Type.WEIGHT,
                unit = WeightUnit.KILOGRAM
            ),
            minLimit = BigDecimal.TEN
        )
    )

    private val rowConditions = listOf(
        TariffNumericCondition(
            measurementUnit = TariffNumericCondition.NumericConditionMeasurement(
                type = TariffNumericCondition.NumericConditionMeasurement.Type.DENSITY,
                unit = DensityUnit.KG_PER_CUBIC_METER
            ),
            minLimit = "100".toBigDecimal(),
            maxLimit = "300".toBigDecimal()
        ),
        TariffNumericCondition(
            measurementUnit = TariffNumericCondition.NumericConditionMeasurement(
                type = TariffNumericCondition.NumericConditionMeasurement.Type.DENSITY,
                unit = DensityUnit.KG_PER_CUBIC_METER
            ),
            minLimit = "300".toBigDecimal(),
            maxLimit = "600".toBigDecimal()
        )
    )

    private val columnConditions = listOf(
        TariffTypeCondition(
            typeId = feature.featureId,
            optionId = feature.options[0].optionId
        ),
        TariffTypeCondition(
            typeId = feature.featureId,
            optionId = feature.options[1].optionId
        ),
    )

    private val tariff = Tariff(
        tariffId = tariffId,
        agentId = agentId,
        applyLevel = Tariff.ApplyLevel.ORDER,
        title = "Delivery",
        description = "Delivery Description",
        features = listOf(feature),
        tariffItems = listOf(
            TariffItem(
                tariffItemId = UUID.randomUUID(),
                conditions = commonConditions + listOf(
                    rowConditions[0], columnConditions[0]
                ),
                pricePerUnit = PricePerUnit(
                    price = Price(
                        value = "1".toBigDecimal(),
                        CurrencyUnit(Currency.USD, emptyMap())
                    ),
                    unit = PricePerUnit.Unit(
                        type = PricePerUnit.Unit.UnitType.FIXED
                    )
                )
            ),
            TariffItem(
                tariffItemId = UUID.randomUUID(),
                conditions = commonConditions + listOf(
                    rowConditions[0], columnConditions[1]
                ),
                pricePerUnit = PricePerUnit(
                    price = Price(
                        value = "2".toBigDecimal(),
                        CurrencyUnit(Currency.USD, emptyMap())
                    ),
                    unit = PricePerUnit.Unit(
                        type = PricePerUnit.Unit.UnitType.FIXED
                    )
                )
            ),
            TariffItem(
                tariffItemId = UUID.randomUUID(),
                conditions = commonConditions + listOf(
                    rowConditions[1], columnConditions[0]
                ),
                pricePerUnit = PricePerUnit(
                    price = Price(
                        value = "3".toBigDecimal(),
                        CurrencyUnit(Currency.USD, emptyMap())
                    ),
                    unit = PricePerUnit.Unit(
                        type = PricePerUnit.Unit.UnitType.FIXED
                    )
                )
            ),
            TariffItem(
                tariffItemId = UUID.randomUUID(),
                conditions = commonConditions + listOf(
                    rowConditions[1], columnConditions[1]
                ),
                pricePerUnit = PricePerUnit(
                    price = Price(
                        value = "4".toBigDecimal(),
                        CurrencyUnit(Currency.USD, emptyMap())
                    ),
                    unit = PricePerUnit.Unit(
                        type = PricePerUnit.Unit.UnitType.FIXED
                    )
                )
            )
        ),
        itemsGroupSizes = listOf(4)
    )

    private val tariffDto = TariffDto(
        tariffId = tariffId,
        agentId = agentId,
        applyLevel = TariffDto.ApplyLevel.ORDER,
        title = "Delivery",
        description = "Delivery Description",
        features = listOf(
            FeatureDto(
                id = feature.featureId,
                title = "Category",
                description = "All products are from one category",
                options = listOf(
                    FeatureOptionDto(
                        id = feature.options[0].optionId,
                        title = "Clothes",
                        description = "Dresses, shorts, shirts, etc"
                    ),
                    FeatureOptionDto(
                        id = feature.options[1].optionId,
                        title = "Home products",
                        description = "Stuff for home"
                    ),
                )
            )
        ),
        tariffTables = listOf(
            TariffTableDto(
                commonConditions = listOf(
                    TariffConditionDto(
                        type = TariffConditionDto.TariffConditionType.NUMERIC,
                        numericCondition = TariffNumericConditionDto(
                            maxLimit = "10000".toBigDecimal(),
                            measurementType = TariffNumericConditionDto.ConditionMeasurementTypeDto.UNITS
                        )
                    ),
                    TariffConditionDto(
                        type = TariffConditionDto.TariffConditionType.NUMERIC,
                        numericCondition = TariffNumericConditionDto(
                            minLimit = "10".toBigDecimal(),
                            measurementType = TariffNumericConditionDto.ConditionMeasurementTypeDto.WEIGHT,
                            measurementUnit = MeasurementUnitDto.KILOGRAM
                        )
                    )
                ),
                rows = listOf(
                    TariffConditionDto(
                        type = TariffConditionDto.TariffConditionType.NUMERIC,
                        numericCondition = TariffNumericConditionDto(
                            minLimit = "100".toBigDecimal(),
                            maxLimit = "300".toBigDecimal(),
                            measurementType = TariffNumericConditionDto.ConditionMeasurementTypeDto.DENSITY,
                            measurementUnit = MeasurementUnitDto.KG_PER_CUBIC_METER
                        )
                    ),
                    TariffConditionDto(
                        type = TariffConditionDto.TariffConditionType.NUMERIC,
                        numericCondition = TariffNumericConditionDto(
                            minLimit = "300".toBigDecimal(),
                            maxLimit = "600".toBigDecimal(),
                            measurementType = TariffNumericConditionDto.ConditionMeasurementTypeDto.DENSITY,
                            measurementUnit = MeasurementUnitDto.KG_PER_CUBIC_METER
                        )
                    )
                ),
                columns = listOf(
                    TariffConditionDto(
                        type = TariffConditionDto.TariffConditionType.ENUM,
                        typeCondition = TariffTypeConditionDto(
                            feature = feature.featureId,
                            option = feature.options[0].optionId
                        )
                    ),
                    TariffConditionDto(
                        type = TariffConditionDto.TariffConditionType.ENUM,
                        typeCondition = TariffTypeConditionDto(
                            feature = feature.featureId,
                            option = feature.options[1].optionId
                        )
                    ),
                ),
                tariffPrices = listOf(
                    listOf(
                        TariffPriceDto(
                            price = PriceDto(
                                value = "1".toBigDecimal(),
                                unit = PriceDto.CurrencyDto.USD
                            ),
                            perUnit = TariffPriceDto.PerUnitDto(
                                unitType = TariffPriceDto.PerUnitDto.UnitType.FIXED,
                                unit = null
                            )
                        ),
                        TariffPriceDto(
                            price = PriceDto(
                                value = "2".toBigDecimal(),
                                unit = PriceDto.CurrencyDto.USD
                            ),
                            perUnit = TariffPriceDto.PerUnitDto(
                                unitType = TariffPriceDto.PerUnitDto.UnitType.FIXED,
                                unit = null
                            )
                        )
                    ),
                    listOf(
                        TariffPriceDto(
                            price = PriceDto(
                                value = "3".toBigDecimal(),
                                unit = PriceDto.CurrencyDto.USD
                            ),
                            perUnit = TariffPriceDto.PerUnitDto(
                                unitType = TariffPriceDto.PerUnitDto.UnitType.FIXED,
                                unit = null
                            )
                        ),
                        TariffPriceDto(
                            price = PriceDto(
                                value = "4".toBigDecimal(),
                                unit = PriceDto.CurrencyDto.USD
                            ),
                            perUnit = TariffPriceDto.PerUnitDto(
                                unitType = TariffPriceDto.PerUnitDto.UnitType.FIXED,
                                unit = null
                            )
                        )
                    )
                )
            )
        )
    )
}