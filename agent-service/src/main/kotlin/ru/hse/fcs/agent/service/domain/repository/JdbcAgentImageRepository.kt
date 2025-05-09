package ru.hse.fcs.agent.service.domain.repository

import org.springframework.data.domain.Page
import org.springframework.data.repository.CrudRepository
import ru.hse.fcs.agent.service.domain.model.AgentImage
import java.util.Optional
import java.util.UUID

interface JdbcAgentImageRepository : CrudRepository<AgentImage, UUID> {

    fun findAllByAgentIdIn(agentIds: List<UUID>): List<AgentImage>

    fun findByAgentIdAndAvatarIsTrue(agentId: UUID): Optional<AgentImage>

    fun findAllByAgentId(agentId: UUID): List<AgentImage>
}