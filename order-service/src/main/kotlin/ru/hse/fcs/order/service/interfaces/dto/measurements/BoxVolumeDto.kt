package ru.hse.fcs.order.service.interfaces.dto.measurements

import java.math.BigDecimal

data class BoxVolumeDto(
    val length: BigDecimal,
    val width: BigDecimal,
    val height: BigDecimal,
    val unit: LengthUnitDto
) {
    enum class LengthUnitDto {
        METRE, CENTIMETRE, MILLIMETRE, FOOT, INCH
    }
}