package ru.hse.fcs.tariff.service.domain.measure.unit

import java.math.BigDecimal

enum class LengthUnit(
    override val baseUnitRate: BigDecimal
) : BasicMeasurementUnit<LengthUnit> {
    METRE(BigDecimal.ONE),
    CENTIMETRE(BigDecimal.valueOf(1, 2)),
    MILLIMETRE(BigDecimal.valueOf(1, 3)),
    FOOT(BigDecimal.valueOf(3048, 4)),
    INCH(BigDecimal.valueOf(254, 4));

    override fun default(): LengthUnit = METRE

    companion object {
        val DEFAULT = METRE
    }
}