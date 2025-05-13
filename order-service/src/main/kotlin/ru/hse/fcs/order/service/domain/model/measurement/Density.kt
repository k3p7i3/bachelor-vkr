package ru.hse.fcs.order.service.domain.model.measurement

import java.math.BigDecimal

data class Density(
    val value: BigDecimal,
    val unit: DensityUnit
) {
    enum class DensityUnit {
        KG_PER_CUBIC_METER,
        GRAM_PER_CUBIC_CM,
        KG_PER_CUBIC_CM,
        GRAM_PER_CUBIC_METER,
        POUNDS_PER_CUBIC_FOOT
    }
}