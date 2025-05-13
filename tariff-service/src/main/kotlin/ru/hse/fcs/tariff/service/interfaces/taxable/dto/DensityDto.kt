package ru.hse.fcs.tariff.service.interfaces.taxable.dto

import java.math.BigDecimal

data class DensityDto(
    val value: BigDecimal,
    val unit: DensityUnitDto
) {
    enum class DensityUnitDto {
        KG_PER_CUBIC_METER,
        GRAM_PER_CUBIC_CM,
        KG_PER_CUBIC_CM,
        GRAM_PER_CUBIC_METER,
        POUNDS_PER_CUBIC_FOOT
    }
}