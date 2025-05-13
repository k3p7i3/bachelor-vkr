package ru.hse.fcs.order.service.interfaces.dto

import ru.hse.fcs.order.service.domain.model.measurement.Price.Currency
import ru.hse.fcs.order.service.interfaces.dto.measurements.PriceDto
import java.math.BigDecimal

data class OrderTariffCostDto(
    var pricePerUnit: PricePerUnitDto,
    var unitAmount: BigDecimal,
    var cost: PriceDto,
    var resultCost: PriceDto
) {
    data class PricePerUnitDto(
        val price: Price,
        val perUnit: PerUnitDto
    ) {

        data class Price(
            val value: BigDecimal,
            val unit: Currency
        )

        data class PerUnitDto(
            val unitType: UnitType,
            val unit: String? = null
        ) {
            enum class UnitType {
                PRODUCT_NUM, PRODUCT_TYPE_UNIT, WEIGHT, VOLUME, DENSITY, FIXED, PERCENTAGE
            }
        }
    }
}