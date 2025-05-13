package ru.hse.fcs.tariff.service.interfaces.tariff.dto

import java.util.*

data class AgentBriefTariffsDto(
    val agentId: UUID,
    val tariffs: List<BriefTariff> = emptyList()
) {
    data class BriefTariff(
        val title: String,
        val price: TariffPriceDto
    )
}