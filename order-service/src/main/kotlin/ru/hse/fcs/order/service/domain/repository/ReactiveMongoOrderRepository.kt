package ru.hse.fcs.order.service.domain.repository

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import ru.hse.fcs.order.service.domain.model.Order
import java.util.UUID

interface ReactiveMongoOrderRepository : ReactiveMongoRepository<Order, UUID> {


}