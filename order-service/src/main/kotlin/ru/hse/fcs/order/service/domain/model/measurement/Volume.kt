package ru.hse.fcs.order.service.domain.model.measurement

import java.math.BigDecimal

data class Volume(
    val value: BigDecimal,
    val unit: VolumeUnit
) {
    enum class VolumeUnit { CUBIC_METER, LITER, MILLILITER, GALLON, QUART, CUBIC_FOOT }
}