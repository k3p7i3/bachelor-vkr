package ru.hse.fcs.tariff.service.application

import org.springframework.stereotype.Component
import ru.hse.fcs.tariff.service.domain.tariff.AgentBriefTariffs
import ru.hse.fcs.tariff.service.domain.tariff.Tariff
import ru.hse.fcs.tariff.service.domain.tariff.TariffRepository
import java.util.*

@Component
class TariffServiceImpl(
    val tariffRepository: TariffRepository
) : TariffService {

    override fun createTariff(tariff: Tariff): Tariff {
        val insertedTariff = tariffRepository.insert(tariff)
        return insertedTariff
    }

    override fun updateTariff(tariff: Tariff): Tariff {
        val updatedTariff = tariffRepository.save(tariff)
        return updatedTariff
    }

    override fun deleteTariff(tariffId: UUID) {
        tariffRepository.deleteById(tariffId)
    }

    override fun getTariff(tariffId: UUID): Tariff {
        return tariffRepository.findById(tariffId).orElseThrow() // TODO
    }

    override fun getAgentTariffs(agentId: UUID): List<Tariff> =
        tariffRepository.findAllByAgentId(agentId)

    override fun getAgentsBriefTariffs(agentIds: List<UUID>): List<AgentBriefTariffs> =
        agentIds.map { agentId ->
            val tariffs = tariffRepository.findAllByAgentId(agentId)
            val briefTariffs = tariffs.map { tariff ->
                val minPriced = tariff.tariffItems.minBy { it.pricePerUnit.price.value }
                AgentBriefTariffs.BriefTariff(
                    title = tariff.title,
                    price = minPriced.pricePerUnit
                )
            }
            AgentBriefTariffs(
                agentId = agentId,
                tariffs = briefTariffs
            )
        }
}