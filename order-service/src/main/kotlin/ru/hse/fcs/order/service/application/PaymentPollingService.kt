package ru.hse.fcs.order.service.application

import jakarta.annotation.PreDestroy
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.Disposable
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.hse.fcs.order.service.config.properties.PaymentClientProperties
import ru.hse.fcs.order.service.domain.model.Order
import ru.hse.fcs.order.service.domain.model.OrderPayment
import ru.hse.fcs.order.service.domain.repository.ReactiveMongoOrderRepository
import ru.hse.fcs.order.service.infrastructure.PaymentClient
import java.util.*
import java.util.concurrent.*


@Service
final class PaymentPollingService(
    private val externalPaymentClient: PaymentClient,
    private val orderRepository: ReactiveMongoOrderRepository,
    private val properties: PaymentClientProperties
) {

    private val activeSubscriptions = ConcurrentHashMap<String, Disposable>();

    fun startPolling(orderId: UUID, paymentExternalId: String) {
        val key = "$orderId $paymentExternalId"
        if (activeSubscriptions.containsKey(key)) {
            logger.warn("Polling already active for order $orderId payment $paymentExternalId")
            return;
        }
        val subscription = pollPaymentStatus(orderId, paymentExternalId)
            .subscribe()
        activeSubscriptions.put(key, subscription)
        logger.info("Started polling for for order $orderId payment $paymentExternalId");
    }

    fun pollPaymentStatus(orderId: UUID, paymentExternalId: String): Flux<Order> {
        return Flux.interval(properties.polling.interval)
            .flatMap { externalPaymentClient.getPaymentStatus(paymentExternalId) }
            .takeUntil { status ->
                status == OrderPayment.Status.SUCCEEDED || status == OrderPayment.Status.CANCELED
            }
            .flatMap { paymentStatus ->
                if (
                    paymentStatus == OrderPayment.Status.SUCCEEDED
                    || paymentStatus == OrderPayment.Status.CANCELED
                ) {
                    orderRepository.findById(orderId)
                        .map { order ->
                            val payment = order.payments
                                .find { it.externalId == paymentExternalId }
                                ?.apply {
                                    status = paymentStatus
                                    if (status == OrderPayment.Status.CANCELED) {
                                        resetCancellation()
                                    }
                                }
                            payment?.let { order.updateWithPayment(payment) }
                            order
                        }
                        .flatMap { order ->
                            orderRepository.save(order)
                        }
                } else Mono.empty()
            }
            .onErrorResume { Mono.empty() }
    }

    private fun cleanupSubscription(orderId: UUID, paymentExternalId: String) {
        val subscription = activeSubscriptions.remove("$orderId $paymentExternalId")
        subscription?.dispose()
        logger.info("Completed polling for order $orderId payment $paymentExternalId");
    }

    @PreDestroy
    fun shutdown() {
        activeSubscriptions.values.forEach { obj -> obj.dispose() }
        activeSubscriptions.clear()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(PaymentPollingService::class.java)
    }
}