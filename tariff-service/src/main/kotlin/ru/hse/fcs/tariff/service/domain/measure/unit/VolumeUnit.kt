package ru.hse.fcs.tariff.service.domain.measure.unit

import java.math.BigDecimal

enum class VolumeUnit(
    override val baseUnitRate: BigDecimal
) : BasicMeasurementUnit<VolumeUnit> {
    CUBIC_METER(BigDecimal.ONE),
    LITER(BigDecimal.valueOf(1, 3)),
    MILLILITER(BigDecimal.valueOf(1, 6)),
    GALLON(BigDecimal.valueOf(378541, 8)),
    QUART(BigDecimal.valueOf(946353, 9)),
    CUBIC_FOOT(BigDecimal.valueOf(283168, 7));

    override fun default(): VolumeUnit = CUBIC_METER

    companion object {
        val DEFAULT = CUBIC_METER
    }
}