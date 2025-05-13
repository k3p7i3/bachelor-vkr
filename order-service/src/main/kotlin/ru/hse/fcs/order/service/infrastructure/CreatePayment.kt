package ru.hse.fcs.order.service.infrastructure

import com.fasterxml.jackson.annotation.JsonValue
import ru.hse.fcs.order.service.domain.model.measurement.Price

data class CreatePayment(
    val amount: Price,
    val description: String? = null,
    val confirmation: CreatePaymentConfirmation,
    val capture: Boolean = true
) {
    data class CreatePaymentConfirmation(
        val type: ConfirmationType,
        val locale: Locale
    ) {
        enum class ConfirmationType(@JsonValue val label: String) {
            EMBEDDED("embedded");
            override fun toString() = this.label
        }

        enum class Locale(@JsonValue val label: String) {
            RU("ru_RU"), EN("en_US");
            override fun toString() = this.label
        }
    }
}