package ru.hse.fcs.agent.service.domain.model

import java.util.UUID

data class AgentBriefInfo(
    val agentId: UUID,
    val name: String,
    val avatar: AgentImage? = null
)