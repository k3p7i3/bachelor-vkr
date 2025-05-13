package ru.hse.fcs.order.service.interfaces.dto.measurements

import java.math.BigDecimal

data class PriceDto(
    val value: BigDecimal,
    val currency: CurrencyDto
) {
    enum class CurrencyDto {
        RUB, CNY, USD, EUR
    }
}