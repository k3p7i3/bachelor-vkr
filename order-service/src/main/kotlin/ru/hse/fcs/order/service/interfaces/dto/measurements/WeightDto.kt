package ru.hse.fcs.order.service.interfaces.dto.measurements

import java.math.BigDecimal

data class WeightDto(
    val value: BigDecimal,
    val unit: WeightUnitDto
) {
    enum class WeightUnitDto {
        KILOGRAM, GRAM, TON, POUND, OUNCE
    }
}