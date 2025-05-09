package ru.hse.fcs.tariff.service.interfaces.taxable.dto

import java.util.*

data class AppliedTariffDataDto(
    val tariffId: UUID,
    val selectedCustomTypeOptions: List<SelectedOptionDto> = emptyList(),
    val finalCurrency: PriceDto.CurrencyDto
) {
    data class SelectedOptionDto(
        val featureId: UUID,
        val optionId: UUID
    )
}