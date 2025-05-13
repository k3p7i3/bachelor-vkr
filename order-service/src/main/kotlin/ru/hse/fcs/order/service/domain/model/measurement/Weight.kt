package ru.hse.fcs.order.service.domain.model.measurement

import java.math.BigDecimal

data class Weight(
    val value: BigDecimal,
    val unit: WeightUnit
) {
    enum class WeightUnit { KILOGRAM, GRAM, TON, POUND, OUNCE }
}