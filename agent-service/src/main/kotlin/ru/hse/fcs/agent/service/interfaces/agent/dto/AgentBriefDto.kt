package ru.hse.fcs.agent.service.interfaces.agent.dto

import java.util.UUID

data class AgentBriefDto(
    val agentId: UUID,
    val name: String,
    val avatar: AgentImageDto? = null
)