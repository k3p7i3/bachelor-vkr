package ru.hse.fcs.order.service.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import ru.hse.fcs.order.service.domain.model.measurement.*
import java.util.*

@Document(collection = "orders")
data class Order(
    @Id
    var id: UUID = UUID.randomUUID(),
    @Indexed
    val agentId: UUID,
    @Indexed
    val clientId: UUID,
    var products: List<OrderProduct> = emptyList(),
    var tariffs: List<OrderTariff> = emptyList(),
    var payments: List<OrderPayment> = emptyList(),
    var weight: Measurement<Weight>? = null,
    var volume: Measurement<Volume>? = null,
    var boxVolume: Measurement<BoxVolume>? = null,
    var density: Measurement<Density>? = null,
    var price: Measurement<Price>? = null,
    var totalNumber: Long? = null,
    var totalCost: Price? = null,
    var paidAmount: Price? = null
) {

    fun updateWithPayment(orderPayment: OrderPayment) {
        tariffs.forEach { tariff ->
            if (orderPayment.includedTariffs.any { it.tariffId == tariff.tariffId }) {
                tariff.updateWithPayment(orderPayment)
            }
        }
        if (orderPayment.status === OrderPayment.Status.SUCCEEDED) {
            paidAmount = paidAmount?.let {
                Price(
                    value = it.value + orderPayment.amount.value,
                    currency = it.currency
                )
            } ?: orderPayment.amount.copy()
        }
    }
}