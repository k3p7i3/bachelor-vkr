package ru.hse.fcs.order.service.infrastructure

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Test
import ru.hse.fcs.order.service.domain.model.OrderPayment
import ru.hse.fcs.order.service.domain.model.measurement.Price
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class PaymentConverterTest {

    @Test
    fun convertPayment() {
        val objectMapper = jacksonObjectMapper()
        val jsonStr = "{\"id\":\"2fb38e6c-000f-5000-8000-1f5004a293ca\",\"status\":\"pending\",\"amount\":{\"value\":\"6650.00\",\"currency\":\"RUB\"},\"description\":\"Искусственносозданныйтариф\",\"recipient\":{\"account_id\":\"1046422\",\"gateway_id\":\"2410387\"},\"created_at\":\"2025-05-12T04:35:56.373Z\",\"confirmation\":{\"type\":\"embedded\",\"confirmation_token\":\"ct-2fb38e6c-000f-5000-8000-1f5004a293ca\"},\"test\":true,\"paid\":false,\"refundable\":false,\"metadata\":{}}"
        val payment = objectMapper.readValue(jsonStr, Payment::class.java)
        assertNotNull(payment)
        assertEquals(
            Payment(
                id = "2fb38e6c-000f-5000-8000-1f5004a293ca",
                status = OrderPayment.Status.PENDING,
                amount = Price(
                    value = "6650.00".toBigDecimal(),
                    currency = Price.Currency.RUB
                ),
                recipient = Payment.Recipient(
                    accountId = "1046422",
                    gatewayId = "2410387"
                ),
                confirmation = Payment.Confirmation(
                    type = Payment.Confirmation.ConfirmationType.EMBEDDED,
                    confirmationToken = "ct-2fb38e6c-000f-5000-8000-1f5004a293ca"
                ),
                test = true,
                paid = false
            ),
            payment
        )
    }
}