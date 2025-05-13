package ru.hse.fcs.order.service.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties(prefix = "payment.client")
data class PaymentClientProperties(
    val credentials: AuthCredentials,
    val url: String,
    val polling: Polling
) {
    data class AuthCredentials(
        val accessKey: String,
        val secretKey: String,
    )

    data class Polling(
        val interval: Duration,
        val timeout: Duration
    )
}