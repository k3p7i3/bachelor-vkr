package ru.hse.fcs.order.service.domain.model

import ru.hse.fcs.order.service.domain.model.measurement.Price
import java.math.BigDecimal

data class OrderTariffCost(
    var pricePerUnit: PricePerUnit,
    var unitAmount: BigDecimal,
    var cost: Price,
    var resultCost: Price
) {

    data class PricePerUnit(
        val price: Price,
        val perUnit: PerUnit
    ) {
        data class PerUnit(
            val unitType: UnitType,
            val unit: String? = null
        ) {
            enum class UnitType {
                PRODUCT_NUM, PRODUCT_TYPE_UNIT, WEIGHT, VOLUME, DENSITY, FIXED, PERCENTAGE
            }
        }
    }
}
