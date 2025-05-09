package ru.hse.fcs.agent.service.domain.repository

import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import ru.hse.fcs.agent.service.domain.model.AgentSelection
import java.util.Optional
import java.util.UUID

interface AgentSelectionRepository : CrudRepository<AgentSelection, UUID> {

    fun findByUserId(userId: UUID): Optional<AgentSelection>

    @Modifying
    @Query("delete from agent_selection where user_id = :userId")
    fun deleteByUserId(userId: UUID): Int
}