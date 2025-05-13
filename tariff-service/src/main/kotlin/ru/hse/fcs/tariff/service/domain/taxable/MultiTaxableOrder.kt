package ru.hse.fcs.tariff.service.domain.taxable

import ru.hse.fcs.tariff.service.domain.measure.Density
import ru.hse.fcs.tariff.service.domain.measure.Price
import ru.hse.fcs.tariff.service.domain.measure.Volume
import ru.hse.fcs.tariff.service.domain.measure.Weight
import java.util.UUID

class MultiTaxableOrder(
    orderId: UUID? = null,
    val taxableProducts: List<MultiTaxableProduct>,
    weight: Weight?,
    volume: Volume?,
    density: Density?,
    price: Price?,
    totalNumber: Long?,
    override val appliedTariffs: List<AppliedTariffData>
) : Order(
    orderId,
    taxableProducts,
    weight,
    volume,
    density,
    price,
    totalNumber,
), MultiTaxableParcel {

    fun splitToTaxableOrders(): List<TaxableOrder> {
        val orderLevel = this.appliedTariffs.map {
            TaxableOrder(
                orderId,
                products,
                weight,
                volume,
                density,
                price,
                totalNumber,
                appliedTariff = it
            )
        }

        val appliedTariffsAtProductLevel = taxableProducts
            .flatMap { it.appliedTariffs }
            .distinctBy { it.tariffId }
            .minus(this.appliedTariffs.toSet())

        val groupedProducts: Map<AppliedTariffData, List<Product>> =
            appliedTariffsAtProductLevel.associateWith { appliedTariff ->
                taxableProducts
                    .filter { product ->
                        product.appliedTariffs.contains(appliedTariff)
                    }
            }

        val taxableOrders = groupedProducts.map { (appliedTariff, selectedProducts) ->
            TaxableOrder(
                orderId,
                selectedProducts,
                appliedTariff = appliedTariff
            )
        }

        return orderLevel + taxableOrders
    }
}