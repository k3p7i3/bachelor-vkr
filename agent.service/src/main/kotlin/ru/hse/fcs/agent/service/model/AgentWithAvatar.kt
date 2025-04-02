package ru.hse.fcs.agent.service.model

data class AgentWithAvatar(
    val agentProfile: AgentProfile,
    val avatar: AgentPicture? = null
)