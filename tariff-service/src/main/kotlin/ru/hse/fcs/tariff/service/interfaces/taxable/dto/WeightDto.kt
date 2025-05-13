package ru.hse.fcs.tariff.service.interfaces.taxable.dto

import java.math.BigDecimal

data class WeightDto(
    val value: BigDecimal,
    val unit: WeightUnitDto
) {
    enum class WeightUnitDto {
        KILOGRAM, GRAM, TON, POUND, OUNCE
    }
}