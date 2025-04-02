package ru.hse.fcs.tariff.service.interfaces.tariff.dto

import java.math.BigDecimal

data class PriceDto(
    val value: BigDecimal,
    val unit: CurrencyDto
) {
    enum class CurrencyDto { RUB, CNY, USD, EUR }
}