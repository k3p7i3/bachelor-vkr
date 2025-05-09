package ru.hse.fcs.order.service.domain.model

import java.util.UUID

data class OrderTariff(
    val tariffId: UUID,
    val applyLevel: Level,
    var isAppliedToWholeOrder: Boolean = true,
    var includedOrderProductIds: List<UUID> = emptyList(),
    var selectedOptions: List<TariffOption> = emptyList(),
    var cost: OrderTariffCost? = null
) {
    enum class Level { ORDER, PRODUCT }

    data class TariffOption(
        val featureId: UUID,
        val optionId: UUID
    )
}