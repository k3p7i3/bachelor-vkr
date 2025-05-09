package ru.hse.fcs.tariff.service.infrastructure

import org.springframework.data.mongodb.repository.MongoRepository
import ru.hse.fcs.tariff.service.domain.tariff.Tariff
import ru.hse.fcs.tariff.service.domain.tariff.TariffRepository
import java.util.*

interface MongoTariffRepository : MongoRepository<Tariff, UUID>, TariffRepository {

    override fun findAllByAgentId(agentId: UUID): List<Tariff>

    override fun insert(tariff: Tariff): Tariff

    override fun save(tariff: Tariff): Tariff

    override fun findById(id: UUID): Optional<Tariff>

    override fun deleteById(id: UUID)
}