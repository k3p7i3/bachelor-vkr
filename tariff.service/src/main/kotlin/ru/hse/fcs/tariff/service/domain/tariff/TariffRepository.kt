package ru.hse.fcs.tariff.service.domain.tariff

import java.util.*

interface TariffRepository { // TODO make an abstract independent repository

    fun insert(tariff: Tariff): Tariff

    fun save(tariff: Tariff): Tariff

    fun findAllByAgentId(agentId: UUID): List<Tariff>

    fun findById(id: UUID): Optional<Tariff>

    fun deleteById(id: UUID)
}