package ru.hse.fcs.agent.service.interfaces.agent.dto

data class AgentDto(
    var profile: AgentProfileDto,
    var rating: AgentRatingDto,
    var avatar: AgentImageDto? = null,
    var images: List<AgentImageDto> = emptyList()
)