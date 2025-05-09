package ru.hse.fcs.order.service.application

import org.springframework.stereotype.Component
import ru.hse.fcs.order.service.domain.model.Order
import ru.hse.fcs.order.service.domain.repository.MongoOrderRepository
import java.util.UUID

@Component
class OrderService(
    private val orderRepository: MongoOrderRepository
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
}