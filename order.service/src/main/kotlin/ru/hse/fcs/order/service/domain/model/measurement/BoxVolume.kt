package ru.hse.fcs.order.service.domain.model.measurement

import java.math.BigDecimal

data class BoxVolume(
    val height: BigDecimal,
    val length: BigDecimal,
    val width: BigDecimal,
    val unit: LengthUnit
) {
    enum class LengthUnit { METRE, CENTIMETRE, MILLIMETRE, FOOT, INCH }
}