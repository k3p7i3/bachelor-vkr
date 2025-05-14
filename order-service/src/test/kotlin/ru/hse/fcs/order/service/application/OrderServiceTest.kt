package ru.hse.fcs.order.service.application

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import ru.hse.fcs.order.service.domain.model.Order
import ru.hse.fcs.order.service.domain.model.OrderPayment
import ru.hse.fcs.order.service.domain.model.OrderTariff
import ru.hse.fcs.order.service.domain.model.measurement.Price
import ru.hse.fcs.order.service.domain.repository.MongoOrderRepository
import ru.hse.fcs.order.service.infrastructure.PaymentClient
import java.time.LocalDateTime
import java.util.Optional
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@ExtendWith(MockitoExtension::class)
class OrderServiceTest {

    @Mock
    private lateinit var orderRepository: MongoOrderRepository

    @Mock
    private lateinit var paymentPollingService: PaymentPollingService

    @Mock
    private lateinit var paymentClient: PaymentClient

    @InjectMocks
    private lateinit var orderService: OrderService

    private val orderId = UUID.randomUUID()
    private val tariffId = UUID.randomUUID()
    private val clientId = UUID.randomUUID()
    private val agentId = UUID.randomUUID()
    private val paymentId = UUID.randomUUID()
    private val testOrder = Order(
        id = orderId,
        clientId = clientId,
        agentId = agentId,
        tariffs = listOf(
            OrderTariff(
                tariffId = tariffId,
                applyLevel = OrderTariff.Level.ORDER,
                paymentStatus = OrderTariff.TariffPaymentStatus.WAITING
            )
        ),
        payments = listOf(
            OrderPayment(
                id = paymentId,
                status = OrderPayment.Status.CREATED,
                includedTariffs = listOf(
                    OrderPayment.TariffPayment(
                        tariffId = tariffId,
                        amount = Price(
                            value = "1450".toBigDecimal(),
                            currency = Price.Currency.RUB
                        )
                    )
                ),
                amount = Price(
                    value = "1450".toBigDecimal(),
                    currency = Price.Currency.RUB
                ),
                createdAt = LocalDateTime.now()
            )
        )
    )

    private val createdPayment = OrderPayment(
        id = paymentId,
        status = OrderPayment.Status.PENDING,
        externalId = "ext_123",
        confirmationToken = "token",
        includedTariffs = listOf(),
        amount = Price(
            value = "1450".toBigDecimal(),
            currency = Price.Currency.RUB
        ),
        createdAt = LocalDateTime.now()
    )

    @Test
    fun `createOrder should insert order`() {
        whenever(orderRepository.insert(testOrder))
            .thenReturn(testOrder)

        val result = orderService.createOrder(testOrder)
        assertEquals(testOrder, result)
        verify(orderRepository).insert(testOrder)
    }

    @Test
    fun `updateOrder should save order`() {
        whenever(orderRepository.save(testOrder))
            .thenReturn(testOrder)

        val result = orderService.updateOrder(testOrder)
        assertEquals(testOrder, result)
        verify(orderRepository).save(testOrder)
    }

    @Test
    fun `getOrder should return order when found`() {
        whenever(orderRepository.findById(orderId))
            .thenReturn(Optional.of(testOrder))
        val result = orderService.getOrder(orderId)
        assertEquals(testOrder, result)
    }

    @Test
    fun `getOrder should throw exception when not found`() {
        whenever(orderRepository.findById(orderId)).thenReturn(Optional.empty())
        assertFailsWith<RuntimeException> {
            orderService.getOrder(orderId)
        }
    }

    @Test
    fun `getClientOrders should return orders for client`() {
        whenever(orderRepository.findAllByClientId(clientId))
            .thenReturn(listOf(testOrder))
        val result = orderService.getClientOrders(clientId)
        assertEquals(1, result.size)
        assertEquals(testOrder, result[0])
    }

    @Test
    fun `getAgentOrders should return orders for agent`() {
        whenever(orderRepository.findAllByAgentId(agentId))
            .thenReturn(listOf(testOrder))
        val result = orderService.getAgentOrders(agentId)
        assertEquals(1, result.size)
        assertEquals(testOrder, result[0])
    }

    @Test
    fun `createPayment should process pending payment`() {
        whenever(orderRepository.findById(orderId))
            .thenReturn(Optional.of(testOrder))
        whenever(paymentClient.createPayment(any<OrderPayment>()))
            .thenAnswer {
                testOrder.payments.first().apply {
                    status = createdPayment.status
                    externalId = createdPayment.externalId
                    confirmationToken = createdPayment.confirmationToken
                }
                createdPayment
            }
            .thenReturn(createdPayment)
        whenever(orderRepository.save(any<Order>()))
            .thenReturn(testOrder)

        val result = orderService.createPayment(orderId, paymentId)
        assertEquals(testOrder.payments[0], result)
        verify(paymentPollingService).startPolling(orderId, "ext_123")
    }

    @Test
    fun `createPayment should reset canceled payment`() {
        val canceledPayment = OrderPayment(
            id = paymentId,
            status = OrderPayment.Status.CANCELED,
            externalId = "ext_123",
            confirmationToken = "token",
            includedTariffs = listOf(),
            amount = Price(
                value = "1450".toBigDecimal(),
                currency = Price.Currency.RUB
            ),
            createdAt = LocalDateTime.now()
        )
        whenever(orderRepository.findById(orderId))
            .thenReturn(Optional.of(testOrder))
        whenever(paymentClient.createPayment(any<OrderPayment>()))
            .thenReturn(canceledPayment)
        whenever(orderRepository.save(any<Order>()))
            .thenReturn(testOrder)

        orderService.createPayment(orderId, paymentId)

        verify(paymentClient).createPayment(any())
        verifyNoInteractions(paymentPollingService)
    }

    @Test
    fun `createPayment should throw when order not found`() {
        whenever(orderRepository.findById(orderId))
            .thenReturn(Optional.empty())
        assertFailsWith<RuntimeException> {
            orderService.createPayment(orderId, paymentId)
        }
    }

    @Test
    fun `createPayment should throw when payment not found`() {
        val wrongPaymentId = UUID.randomUUID()
        whenever(orderRepository.findById(orderId))
            .thenReturn(Optional.of(testOrder))
        assertFailsWith<RuntimeException> {
            orderService.createPayment(orderId, wrongPaymentId)
        }
    }
}