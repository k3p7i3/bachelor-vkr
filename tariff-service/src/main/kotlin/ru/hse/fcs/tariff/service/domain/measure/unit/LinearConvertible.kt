package ru.hse.fcs.tariff.service.domain.measure.unit

import java.math.BigDecimal

sealed interface LinearConvertible<in T> {
    fun getConversionRateTo(other: T): BigDecimal
}




