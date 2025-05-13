package ru.hse.fcs.agent.service.interfaces.agent.dto

import java.time.LocalDateTime
import java.util.*

data class AgentImageDto(
    val id: UUID,
    val agentId: UUID,
    val presignedUrl: String? = null,
    val expire: LocalDateTime? = null,
    val createdAt: LocalDateTime,
)