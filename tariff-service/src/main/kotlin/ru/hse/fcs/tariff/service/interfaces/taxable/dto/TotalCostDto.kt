package ru.hse.fcs.tariff.service.interfaces.taxable.dto

class TotalCostDto(
    val complexOrder: OrderDto,
    val calculatedTariffs: List<CalculatedTariff>,
    val totalCost: PriceDto? = null
) {

    data class CalculatedTariff(
        val order: OrderDto,
        val cost: CostDto? = null
    )
}