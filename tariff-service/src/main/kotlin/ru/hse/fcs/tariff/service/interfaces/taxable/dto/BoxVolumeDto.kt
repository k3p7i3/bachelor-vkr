package ru.hse.fcs.tariff.service.interfaces.taxable.dto

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