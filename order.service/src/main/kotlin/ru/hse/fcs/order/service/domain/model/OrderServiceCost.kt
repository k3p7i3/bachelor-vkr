package ru.hse.fcs.order.service.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import ru.hse.fcs.order.service.domain.model.measurement.Price
import java.math.BigDecimal
import java.util.UUID

@Table("order_service_cost")
data class OrderServiceCost(

    @Id
    @Column("id")
    val id: UUID? = null,

    @Column("order_service_id")
    val orderServiceId: UUID,

    @Column("is_approximate")
    val isApproximate: Boolean = true,

    @Column("price_per_unit")
    val pricePerUnit: PricePerUnit,

    @Column("unit_amount")
    val unitAmount: BigDecimal,

    @Column("cost")
    val cost: Price
) {

    data class PricePerUnit(
        val price: Price,
        val unitType: UnitType,
        val unit: String? = null
    ) {
        enum class UnitType {
            PRODUCT_NUM, PRODUCT_TYPE_UNIT, WEIGHT, VOLUME, DENSITY, FIXED, PERCENTAGE
        }
    }
}
