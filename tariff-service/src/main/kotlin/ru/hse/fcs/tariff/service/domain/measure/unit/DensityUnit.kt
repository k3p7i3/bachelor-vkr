package ru.hse.fcs.tariff.service.domain.measure.unit

import java.math.BigDecimal

enum class DensityUnit(
    override val baseUnitRate: BigDecimal
) : BasicMeasurementUnit<DensityUnit> {
    KG_PER_CUBIC_METER(BigDecimal.ONE),
    GRAM_PER_CUBIC_CM(BigDecimal.valueOf(1000)),
    KG_PER_CUBIC_CM(BigDecimal.valueOf(1000000)),
    GRAM_PER_CUBIC_METER(BigDecimal.valueOf(1, 3)),
    POUNDS_PER_CUBIC_FOOT(BigDecimal.valueOf(16018463, 6));

    override fun default(): DensityUnit = KG_PER_CUBIC_METER

    companion object {
        val DEFAULT = KG_PER_CUBIC_METER
    }
}