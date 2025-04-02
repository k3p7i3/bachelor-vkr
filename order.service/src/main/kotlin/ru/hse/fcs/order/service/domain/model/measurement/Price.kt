package ru.hse.fcs.order.service.domain.model.measurement

import java.math.BigDecimal

data class Price(
    val value: BigDecimal,
    val currency: Currency
) {
    enum class Currency { RUB, EUR, CNY, USD }
}