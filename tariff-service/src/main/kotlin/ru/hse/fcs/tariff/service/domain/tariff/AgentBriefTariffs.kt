package ru.hse.fcs.tariff.service.domain.tariff

import java.util.UUID

data class AgentBriefTariffs(
    val agentId: UUID,
    val tariffs: List<BriefTariff> = emptyList()
) {
    data class BriefTariff(
        val title: String,
        val price: PricePerUnit
    )
}