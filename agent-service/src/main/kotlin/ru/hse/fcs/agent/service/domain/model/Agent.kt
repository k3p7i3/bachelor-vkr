package ru.hse.fcs.agent.service.domain.model

data class Agent(
    val profile: AgentProfile,
    val avatar: AgentImage? = null,
    val images: List<AgentImage> = emptyList(),
    val rating: AgentRating
)