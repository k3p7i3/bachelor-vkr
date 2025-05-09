package ru.hse.fcs.tariff.service.domain.measure.unit

import java.math.BigDecimal

sealed interface BasicMeasurementUnit<T> : LinearConvertible<BasicMeasurementUnit<T>> {
    val baseUnitRate: BigDecimal

    fun default(): T

    override fun getConversionRateTo(other: BasicMeasurementUnit<T>): BigDecimal =
        baseUnitRate.divide(other.baseUnitRate)
}