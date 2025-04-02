package ru.hse.fcs.tariff.service.application

import org.springframework.stereotype.Component
import ru.hse.fcs.tariff.service.domain.tariff.Tariff
import ru.hse.fcs.tariff.service.domain.tariff.TariffRepository
import java.util.*

@Component
class TariffServiceImpl(
    val tariffRepository: TariffRepository
) : TariffService {

    override fun createTariff(tariff: Tariff): Tariff {
        // TODO - validate tariff??
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
}