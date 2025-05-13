package ru.hse.fcs.order.service.interfaces.dto

import ru.hse.fcs.order.service.domain.model.OrderTariff
import ru.hse.fcs.order.service.domain.model.OrderTariff.TariffPaymentStatus
import ru.hse.fcs.order.service.interfaces.dto.measurements.PriceDto
import java.util.*

data class OrderTariffDto(
    var tariffId: UUID,
    var applyLevel: OrderTariff.Level,
    var isAppliedToWholeOrder: Boolean = true,
    var includedOrderProductIds: List<UUID> = emptyList(),
    var selectedOptions: List<TariffOptionDto> = emptyList(),
    var cost: OrderTariffCostDto? = null,
    var isFixed: Boolean = false,
    var paymentStatus: TariffPaymentStatus = TariffPaymentStatus.NOT_PAID,
    var paidAmount: PriceDto? = null
) {
    data class TariffOptionDto(
        var featureId: UUID,
        var optionId: UUID
    )
}