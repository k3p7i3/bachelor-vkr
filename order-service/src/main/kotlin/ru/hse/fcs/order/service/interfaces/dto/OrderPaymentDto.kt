package ru.hse.fcs.order.service.interfaces.dto

import ru.hse.fcs.order.service.domain.model.OrderPayment
import ru.hse.fcs.order.service.domain.model.measurement.Price
import ru.hse.fcs.order.service.interfaces.dto.measurements.PriceDto
import java.time.LocalDateTime
import java.util.UUID

data class OrderPaymentDto(
    var id: UUID,
    var externalId: String? = null,
    var confirmationToken: String? = null,
    var status: OrderPayment.Status,
    var description: String? = null,
    var includedTariffs: List<TariffPaymentDto>,
    var amount: PriceDto,
    var createdAt: LocalDateTime
) {

    data class TariffPaymentDto(
        var tariffId: UUID,
        var amount: Price
    )
}