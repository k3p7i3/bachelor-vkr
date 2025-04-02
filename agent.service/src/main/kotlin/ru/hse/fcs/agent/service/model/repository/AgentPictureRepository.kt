package ru.hse.fcs.agent.service.model.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.hse.fcs.agent.service.model.AgentPicture
import java.util.UUID

interface AgentPictureRepository : JpaRepository<AgentPicture, UUID> {

    fun findAvatarTrueAndAgentIdIn(agentId: List<UUID>): List<AgentPicture>

    fun findByAgentIdAndAvatarTrue(agentId: UUID): AgentPicture?
}