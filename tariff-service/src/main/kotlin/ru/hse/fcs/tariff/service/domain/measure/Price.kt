package ru.hse.fcs.tariff.service.domain.measure

import ru.hse.fcs.tariff.service.domain.measure.unit.CurrencyUnit
import java.math.BigDecimal

class Price(
    override val value: BigDecimal,
    override val unit: CurrencyUnit
) : ConvertibleMeasurement<CurrencyUnit> {

    override fun convertTo(other: CurrencyUnit): ConvertibleMeasurement<CurrencyUnit> {
        val rate = unit.getConversionRateTo(other)
        return Price(
            value = value.multiply(rate),
            unit = other
        )
    }

    override operator fun plus(other: ConvertibleMeasurement<CurrencyUnit>): Price {
        val rate = other.unit.getConversionRateTo(unit)
        return Price(
            value + other.value.multiply(rate),
            unit
        )
    }
}