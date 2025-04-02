package ru.hse.fcs.order.service.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.UUID

@Table("order_service")
data class OrderService(
    @Id
    @Column("id")
    val id: UUID? = null,

    @Column("order_id")
    val orderId: UUID,

    @Column("tariff_id")
    val tariffId: UUID,

    @Column("tariff_level")
    val tariffLevel: Level,

    @Column("is_applied_to_whole_order")
    val isAppliedToWholeOrder: Boolean = true,

    @Column("include_order_product_ids")
    val includedOrderProductIds: List<UUID>? = null,

    @Column("selected_options")
    val selectedOptions: List<TariffOption> = emptyList()
) {

    enum class Level { ORDER, PRODUCT }

    data class TariffOption(
        val featureId: UUID,
        val featureTitle: String,
        val optionId: UUID,
        val optionTitle: String
    )
}