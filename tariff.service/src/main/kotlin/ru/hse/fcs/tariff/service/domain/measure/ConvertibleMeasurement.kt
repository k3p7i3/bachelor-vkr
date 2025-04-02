package ru.hse.fcs.tariff.service.domain.measure

import ru.hse.fcs.tariff.service.domain.measure.unit.LinearConvertible
import java.math.BigDecimal

interface ConvertibleMeasurement<T : LinearConvertible<T>> {
    val value: BigDecimal
    val unit: T

    fun convertTo(other: T): ConvertibleMeasurement<T>

    operator fun plus(other: ConvertibleMeasurement<T>): ConvertibleMeasurement<T>
}