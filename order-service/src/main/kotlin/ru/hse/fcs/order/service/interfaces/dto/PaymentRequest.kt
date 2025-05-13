package ru.hse.fcs.order.service.interfaces.dto

import java.util.UUID

data class PaymentRequest(
    val orderId: UUID,
    val paymentId: UUID
)