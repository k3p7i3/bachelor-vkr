package ru.hse.fcs.tariff.service.domain.taxable

import ru.hse.fcs.tariff.service.domain.measure.Price

class TotalCost(
    val complexOrder: MultiTaxableOrder,
    val calculatedTaxableParcels: List<CalculatedTaxableParcel>,
    val totalCost: Price? = null
) {
    data class CalculatedTaxableParcel(
        val taxableOrder: TaxableOrder,
        val cost: Cost? = null
    )
}