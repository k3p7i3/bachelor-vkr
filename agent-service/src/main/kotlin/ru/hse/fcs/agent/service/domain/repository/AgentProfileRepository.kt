package ru.hse.fcs.agent.service.domain.repository

import org.springframework.data.repository.CrudRepository
import ru.hse.fcs.agent.service.domain.model.AgentProfile
import java.util.UUID

interface AgentProfileRepository : CrudRepository<AgentProfile, UUID> {

    fun findAllByIdIn(ids: List<UUID>): List<AgentProfile>
}