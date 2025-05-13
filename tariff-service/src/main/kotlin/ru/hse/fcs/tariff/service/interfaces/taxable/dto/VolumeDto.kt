package ru.hse.fcs.tariff.service.interfaces.taxable.dto

import java.math.BigDecimal

data class VolumeDto(
    val value: BigDecimal,
    val unit: VolumeUnitDto
) {
    enum class VolumeUnitDto {
        CUBIC_METER, LITER, MILLILITER, GALLON, QUART, CUBIC_FOOT
    }
}