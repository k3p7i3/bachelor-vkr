package ru.hse.fcs.order.service.domain.model

import ru.hse.fcs.order.service.domain.model.measurement.Price
import java.util.UUID

data class OrderTariff(
    val tariffId: UUID,
    val applyLevel: Level,
    var isAppliedToWholeOrder: Boolean = true,
    var includedOrderProductIds: List<UUID> = emptyList(),
    var selectedOptions: List<TariffOption> = emptyList(),
    var cost: OrderTariffCost? = null,
    var isFixed: Boolean = false,
    var paymentStatus: TariffPaymentStatus = TariffPaymentStatus.NOT_PAID,
    var paidAmount: Price? = null
) {
    enum class Level { ORDER, PRODUCT }

    enum class TariffPaymentStatus {
        NOT_PAID, WAITING, PAID_PARTIALLY, PAID
    }

    data class TariffOption(
        val featureId: UUID,
        val optionId: UUID
    )

    fun updateWithPayment(orderPayment: OrderPayment) {
        this.isFixed = true
        when (orderPayment.status) {
            OrderPayment.Status.CREATED,
            OrderPayment.Status.PENDING,
            OrderPayment.Status.WAITING_FOR_CAPTURE -> {
                this.paymentStatus = TariffPaymentStatus.WAITING
            }

            OrderPayment.Status.SUCCEEDED -> {
                val tariffPaymentAmount = orderPayment.includedTariffs
                    .find { it.tariffId == this.tariffId }!!
                    .amount

                this.cost?.let {
                    if (it.resultCost.value <= tariffPaymentAmount.value) {
                        this.paymentStatus = TariffPaymentStatus.PAID
                    } else {
                        this.paymentStatus = TariffPaymentStatus.PAID_PARTIALLY
                    }
                } ?: {
                    this.paymentStatus = TariffPaymentStatus.PAID
                }
                this.paidAmount = tariffPaymentAmount
            }

            OrderPayment.Status.CANCELED -> {
                this.paymentStatus = TariffPaymentStatus.WAITING
            }
        }
    }
}