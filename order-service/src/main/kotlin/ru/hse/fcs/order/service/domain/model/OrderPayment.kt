package ru.hse.fcs.order.service.domain.model

import com.fasterxml.jackson.annotation.JsonValue
import ru.hse.fcs.order.service.domain.model.measurement.Price
import java.time.LocalDateTime
import java.util.UUID

data class OrderPayment(
    var id: UUID,
    var externalId: String? = null,
    var confirmationToken: String? = null,
    var status: Status,
    var description: String? = null,
    var includedTariffs: List<TariffPayment>,
    var amount: Price,
    var createdAt: LocalDateTime
) {

    data class TariffPayment(
        var tariffId: UUID,
        var amount: Price
    )

    enum class Status(@JsonValue val label: String) {
        CREATED("created"),
        PENDING("pending"),
        WAITING_FOR_CAPTURE("waiting_for_capture"),
        SUCCEEDED("succeeded"),
        CANCELED("canceled");
    }

    fun resetCancellation() {
        id = UUID.randomUUID()
        status = Status.CREATED
        confirmationToken = null
        externalId = null
    }
}