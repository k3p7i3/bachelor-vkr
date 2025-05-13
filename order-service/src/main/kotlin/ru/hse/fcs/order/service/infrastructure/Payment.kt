package ru.hse.fcs.order.service.infrastructure

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import ru.hse.fcs.order.service.domain.model.OrderPayment
import ru.hse.fcs.order.service.domain.model.measurement.Price
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class Payment(
    val id: String,
    val status: OrderPayment.Status,
    val amount: Price,
    val recipient: Recipient,
    val test: Boolean,
    val confirmation: Confirmation? = null,
    val paid: Boolean
) {
    data class Recipient(
        @JsonProperty("account_id")
        val accountId: String,
        @JsonProperty("gateway_id")
        val gatewayId: String
    )

    data class Confirmation(
        val type: ConfirmationType,
        @JsonProperty("confirmation_token")
        val confirmationToken: String
    ) {
        enum class ConfirmationType(@JsonValue val label: String) {
            EMBEDDED("embedded");
            override fun toString() = this.label
        }
    }

    fun updateOrderPayment(orderPayment: OrderPayment): OrderPayment {
        orderPayment.externalId = this.id
        orderPayment.status = this.status
        orderPayment.amount = this.amount
        orderPayment.confirmationToken = this.confirmation?.confirmationToken

        return orderPayment
    }
}