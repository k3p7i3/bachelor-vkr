package ru.hse.fcs.tariff.service.domain.taxable

class TotalCost(
    val complexOrder: MultiTaxableParcel,
    val calculatedTaxableParcels: List<CalculatedTaxableParcel>
) {
    data class CalculatedTaxableParcel(
        val taxableParcel: TaxableParcel,
        val cost: Cost
    )
}