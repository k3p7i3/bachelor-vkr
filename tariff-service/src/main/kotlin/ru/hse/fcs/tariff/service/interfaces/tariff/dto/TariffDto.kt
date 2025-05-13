package ru.hse.fcs.tariff.service.interfaces.tariff.dto

import java.util.UUID

data class TariffDto(
    val tariffId: UUID? = null,
    val agentId: UUID,
    val applyLevel: ApplyLevel,
    val title: String,
    val description: String? = null,
    val features: List<FeatureDto> = emptyList(),
    val tariffTables: List<TariffTableDto> = emptyList()
) {
    enum class ApplyLevel { ORDER, PRODUCT }
}



