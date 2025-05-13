package ru.hse.fcs.tariff.service.interfaces.tariff.dto

data class TariffPriceDto(
    val price: PriceDto,
    val perUnit: PerUnitDto
) {
    data class PerUnitDto(
        val unitType: UnitType,
        val unit: MeasurementUnitDto?
    ) {
        enum class UnitType {
            PRODUCT_NUM, PRODUCT_TYPE_UNIT, WEIGHT, VOLUME, DENSITY, FIXED, PERCENTAGE
        }
    }
}