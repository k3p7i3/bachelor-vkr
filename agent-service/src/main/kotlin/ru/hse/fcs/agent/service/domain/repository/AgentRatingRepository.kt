package ru.hse.fcs.agent.service.domain.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import ru.hse.fcs.agent.service.domain.model.AgentRating
import java.util.Optional
import java.util.UUID

interface AgentRatingRepository : PagingAndSortingRepository<AgentRating, UUID>, CrudRepository<AgentRating, UUID> {
    fun findByAgentId(agentId: UUID): Optional<AgentRating>
}