package ru.hse.fcs.order.service.infrastructure

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import ru.hse.fcs.order.service.config.properties.PaymentClientProperties
import ru.hse.fcs.order.service.domain.model.OrderPayment

@Service
class PaymentClient(
    private val restTemplate: RestTemplate,
    private val webClient: WebClient,
    private val properties: PaymentClientProperties
) {
    fun createPayment(orderPayment: OrderPayment): OrderPayment {
        val endpointUrl = properties.url

        val headers = HttpHeaders()
        headers.set("Idempotence-Key", orderPayment.id.toString())
        val createPaymentRequest = CreatePayment(
            amount = orderPayment.amount,
            description = orderPayment.description,
            confirmation = CreatePayment.CreatePaymentConfirmation(
                type = CreatePayment.CreatePaymentConfirmation.ConfirmationType.EMBEDDED,
                locale = CreatePayment.CreatePaymentConfirmation.Locale.RU
            ),
            capture = true
        )
        val requestEntity = HttpEntity(createPaymentRequest, headers)

        val response = restTemplate.exchange(
            endpointUrl,
            HttpMethod.POST,
            requestEntity,
            Payment::class.java
        )
        if (response.statusCode.isError) {
            throw RuntimeException("Payment error")
        }
        val payment = response.body!!
        payment.updateOrderPayment(orderPayment)
        return orderPayment
    }

    fun getPaymentStatus(externalId: String): Mono<OrderPayment.Status> {
        return webClient.get()
            .uri(properties.url + "/$externalId")
            .retrieve()
            .onStatus(HttpStatusCode::isError) { response ->
                response.bodyToMono(String::class.java)
                    .flatMap { body ->
                        Mono.error(
                            RuntimeException("Failed to get payment status: $body")
                        )
                    }
            }
            .bodyToMono(Payment::class.java)
            .timeout(properties.polling.timeout)
            .map { it.status }
    }
}