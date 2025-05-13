package ru.hse.fcs.tariff.service.domain.measure

import ru.hse.fcs.tariff.service.domain.measure.unit.LengthUnit
import ru.hse.fcs.tariff.service.domain.measure.unit.VolumeUnit
import java.math.BigDecimal

class Volume(
    override val value: BigDecimal,
    override val unit: VolumeUnit
) : ConvertibleMeasurement<VolumeUnit> {

    constructor(boxVolume: BoxVolume) : this(
        value = boxVolume.calculateVolume(),
        unit = VolumeUnit.DEFAULT
    )

    override fun convertTo(other: VolumeUnit): ConvertibleMeasurement<VolumeUnit> {
        val rate = unit.getConversionRateTo(other)
        return Volume(
            value = value.multiply(rate),
            unit = other
        )
    }

    override operator fun plus(other: ConvertibleMeasurement<VolumeUnit>): Volume {
        val rate = other.unit.getConversionRateTo(unit)
        return Volume(
            value + other.value.multiply(rate),
            unit
        )
    }

    class BoxVolume(
        val length: BigDecimal,
        val width: BigDecimal,
        val height: BigDecimal,
        val unit: LengthUnit
    ) {
        fun calculateVolume(): BigDecimal {
            val rate = unit.getConversionRateTo(unit.default())
            return listOf(length, width, height)
                .map {
                    it.multiply(rate)
                }
                .reduce { acc, dimension ->
                    acc.multiply(dimension)
                }
        }

    }
}