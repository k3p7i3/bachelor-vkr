package ru.hse.fcs.order.service.interfaces.dto.measurements

import java.math.BigDecimal

data class VolumeDto(
    val value: BigDecimal,
    val unit: VolumeUnitDto
) {
    enum class VolumeUnitDto {
        CUBIC_METER, LITER, MILLILITER, GALLON, QUART, CUBIC_FOOT
    }
}