package ru.hse.fcs.order.service.domain.repository

import org.springframework.data.mongodb.repository.MongoRepository
import ru.hse.fcs.order.service.domain.model.Order
import java.util.*

interface MongoOrderRepository : MongoRepository<Order, UUID> {

    fun findAllByClientId(clientId: UUID): List<Order>

    fun findAllByAgentId(agentId: UUID): List<Order>
}