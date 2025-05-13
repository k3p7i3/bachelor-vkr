package ru.hse.fcs.order.service.application

import org.springframework.stereotype.Component
import ru.hse.fcs.order.service.domain.model.Order
import ru.hse.fcs.order.service.domain.model.OrderPayment
import ru.hse.fcs.order.service.domain.repository.MongoOrderRepository
import ru.hse.fcs.order.service.infrastructure.PaymentClient
import java.util.UUID

@Component
class OrderService(
    private val orderRepository: MongoOrderRepository,
    private val paymentPollingService: PaymentPollingService,
    private val paymentClient: PaymentClient
) {

    fun createOrder(order: Order): Order {
        val createdOrder = orderRepository.insert(order)
        return createdOrder
    }

    fun updateOrder(order: Order): Order {
        val savedOrder = orderRepository.save(order)
        return savedOrder
    }

    fun getOrder(orderId: UUID): Order {
        val order = orderRepository.findById(orderId)
        if (order.isEmpty) {
            throw RuntimeException("Order with id=$orderId does not exist")
        }
        return order.get()
    }

    fun getClientOrders(clientId: UUID): List<Order> {
        val orders = orderRepository.findAllByClientId(clientId)
        return orders
    }

    fun getAgentOrders(agentId: UUID): List<Order> {
        val orders = orderRepository.findAllByAgentId(agentId)
        return orders
    }

    fun createPayment(orderId: UUID, paymentId: UUID): OrderPayment {
        val order = orderRepository.findById(orderId)
            .orElseThrow { RuntimeException("Order $orderId is not found") }

        val payment = order.payments.find { it.id == paymentId }
        payment?.let {
            val createdPayment = paymentClient.createPayment(payment)
            if (payment.status == OrderPayment.Status.CANCELED) {
                payment.resetCancellation()
            }
            order.updateWithPayment(createdPayment)
            orderRepository.save(order);

            if (payment.status == OrderPayment.Status.PENDING ||
                payment.status == OrderPayment.Status.WAITING_FOR_CAPTURE
            ) {
                paymentPollingService.startPolling(
                    order.id,
                    createdPayment.externalId!!
                )
            }
        } ?: throw RuntimeException("No payment with id=$paymentId found")

        return payment
    }
}