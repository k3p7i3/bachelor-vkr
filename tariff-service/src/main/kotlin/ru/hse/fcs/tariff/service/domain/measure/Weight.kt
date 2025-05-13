package ru.hse.fcs.tariff.service.domain.measure

import ru.hse.fcs.tariff.service.domain.measure.unit.WeightUnit
import java.math.BigDecimal

class Weight(
    override val value: BigDecimal,
    override val unit: WeightUnit
) : ConvertibleMeasurement<WeightUnit> {

    override fun convertTo(other: WeightUnit): Weight {
        val rate = unit.getConversionRateTo(other)
        return Weight(
            value = value.multiply(rate),
            unit = other
        )
    }

    override operator fun plus(other: ConvertibleMeasurement<WeightUnit>): Weight {
        val rate = other.unit.getConversionRateTo(unit)
        return Weight(
            value + other.value.multiply(rate),
            unit
        )
    }
}