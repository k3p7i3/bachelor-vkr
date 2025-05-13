package ru.hse.fcs.tariff.service.domain.measure.unit

import java.math.BigDecimal

enum class WeightUnit(
    override val baseUnitRate: BigDecimal
) : BasicMeasurementUnit<WeightUnit> {
    KILOGRAM(BigDecimal.ONE),
    GRAM(BigDecimal.valueOf(1, 3)),
    TON(BigDecimal.valueOf(1000)),
    POUND(BigDecimal.valueOf(45359237, 8)),
    OUNCE(BigDecimal.valueOf(28349523125, 12));

    override fun default(): WeightUnit = KILOGRAM

    companion object {
        val DEFAULT = KILOGRAM
    }
}