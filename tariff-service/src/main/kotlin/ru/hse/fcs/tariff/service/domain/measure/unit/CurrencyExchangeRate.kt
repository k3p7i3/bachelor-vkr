package ru.hse.fcs.tariff.service.domain.measure.unit

import java.math.BigDecimal

data class CurrencyExchangeRate(
    val from: Currency,
    val to: Currency,
    val rate: BigDecimal
)