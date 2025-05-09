package ru.hse.fcs.tariff.service.domain.measure

import ru.hse.fcs.tariff.service.domain.measure.unit.DensityUnit
import ru.hse.fcs.tariff.service.domain.measure.unit.VolumeUnit
import ru.hse.fcs.tariff.service.domain.measure.unit.WeightUnit
import java.math.BigDecimal
import java.math.RoundingMode

class Density(
    override val value: BigDecimal,
    override val unit: DensityUnit
) : ConvertibleMeasurement<DensityUnit> {
    constructor(weight: Weight, volume: Volume) : this(
        value = weight.convertTo(WeightUnit.DEFAULT).value
            .divide(volume.convertTo(VolumeUnit.DEFAULT).value, 5, RoundingMode.HALF_UP),
        unit = DensityUnit.DEFAULT
    )

    override fun convertTo(other: DensityUnit): ConvertibleMeasurement<DensityUnit> {
        val rate = unit.getConversionRateTo(other)
        return Density(
            value = value.multiply(rate),
            unit = other
        )
    }

    override operator fun plus(other: ConvertibleMeasurement<DensityUnit>): Density {
        val rate = other.unit.getConversionRateTo(unit)
        return Density(
            value + other.value.multiply(rate),
            unit
        )
    }
}