package ru.hse.fcs.tariff.service.application

import ru.hse.fcs.tariff.service.domain.tariff.AgentBriefTariffs
import ru.hse.fcs.tariff.service.domain.tariff.Tariff
import java.util.UUID

interface TariffService {

    fun createTariff(tariff: Tariff): Tariff

    fun updateTariff(tariff: Tariff): Tariff

    fun deleteTariff(tariffId: UUID)

    fun getTariff(tariffId: UUID): Tariff

    fun getAgentTariffs(agentId: UUID): List<Tariff>

    fun getAgentsBriefTariffs(agentIds: List<UUID>): List<AgentBriefTariffs>
}

