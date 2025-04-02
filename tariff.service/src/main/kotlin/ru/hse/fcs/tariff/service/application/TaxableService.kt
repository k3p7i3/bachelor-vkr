package ru.hse.fcs.tariff.service.application

import org.springframework.stereotype.Component
import ru.hse.fcs.tariff.service.domain.tariff.TariffRepository
import ru.hse.fcs.tariff.service.domain.taxable.MultiTaxableOrder
import ru.hse.fcs.tariff.service.domain.taxable.TotalCost

@Component
class TaxableService(
    val tariffRepository: TariffRepository
) {
    fun calculateTotalCost(order: MultiTaxableOrder): TotalCost {
        val taxableOrders = order.splitToTaxableOrders()

        val calculatedParcels = taxableOrders.mapNotNull { taxableOrder ->
            val tariff = tariffRepository.findById(
                taxableOrder.appliedTariff.tariffId
            )

            if (tariff.isPresent) {
                val cost = tariff.get().calculateCost(taxableOrder)!!

                TotalCost.CalculatedTaxableParcel(
                    taxableParcel = taxableOrder,
                    cost = cost
                )
            } else null
        }

        return TotalCost(
            complexOrder = order,
            calculatedTaxableParcels = calculatedParcels
        )
    }
}