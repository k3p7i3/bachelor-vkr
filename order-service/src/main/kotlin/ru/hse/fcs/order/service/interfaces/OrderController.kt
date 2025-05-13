package ru.hse.fcs.order.service.interfaces

import org.springframework.core.convert.converter.Converter
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.hse.fcs.order.service.application.OrderService
import ru.hse.fcs.order.service.domain.model.Order
import ru.hse.fcs.order.service.domain.model.OrderPayment
import ru.hse.fcs.order.service.interfaces.dto.OrderDto
import ru.hse.fcs.order.service.interfaces.dto.OrderPaymentDto
import ru.hse.fcs.order.service.interfaces.dto.PaymentRequest
import java.util.UUID

@RestController
@RequestMapping("/api/order")
class OrderController(
    private val orderService: OrderService,
    private val fromDtoConverter: Converter<OrderDto, Order>,
    private val toDtoConverter: Converter<Order, OrderDto>,
    private val paymentConverter: Converter<OrderPayment, OrderPaymentDto>
) {

    @PostMapping
    fun createOrder(@RequestBody order: OrderDto): ResponseEntity<OrderDto> {
        val savedOrder = orderService.createOrder(fromDtoConverter.convert(order)!!)
        return ResponseEntity.ok(toDtoConverter.convert(savedOrder))
    }

    @PutMapping
    fun updateOrder(@RequestBody order: OrderDto): ResponseEntity<OrderDto> {
        val savedOrder = orderService.updateOrder(fromDtoConverter.convert(order)!!)
        return ResponseEntity.ok(toDtoConverter.convert(savedOrder))
    }

    @GetMapping
    fun getOrder(@RequestParam orderId: UUID): ResponseEntity<OrderDto> {
        val order = orderService.getOrder(orderId)
        return ResponseEntity.ok(toDtoConverter.convert(order))
    }

    @GetMapping("/client")
    fun getClientOrders(@RequestParam clientId: UUID): ResponseEntity<List<OrderDto>> {
        val orders = orderService.getClientOrders(clientId).map { toDtoConverter.convert(it)!! }
        return ResponseEntity.ok(orders)
    }

    @GetMapping("/agent")
    fun getAgentOrders(@RequestParam agentId: UUID): ResponseEntity<List<OrderDto>> {
        val orders = orderService.getAgentOrders(agentId).map { toDtoConverter.convert(it)!! }
        return ResponseEntity.ok(orders)
    }

    @PostMapping("/payment")
    fun executePayment(@RequestBody body: PaymentRequest): ResponseEntity<OrderPaymentDto> {
        val payment = orderService.createPayment(body.orderId, body.paymentId)
        return ResponseEntity.ok(paymentConverter.convert(payment))
    }
}