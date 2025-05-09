package ru.hse.fcs.tariff.service.interfaces.taxable.dto

import java.math.BigDecimal

data class PriceDto(
    val value: BigDecimal,
    val currency: CurrencyDto
) {
    enum class CurrencyDto {
        RUB, CNY, USD, EUR
    }
}