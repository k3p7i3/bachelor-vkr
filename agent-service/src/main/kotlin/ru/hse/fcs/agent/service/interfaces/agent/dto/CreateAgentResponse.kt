package ru.hse.fcs.agent.service.interfaces.agent.dto

import ru.hse.fcs.agent.service.application.dto.UserDto

data class CreateAgentResponse(
    val agent: AgentDto,
    val user: UserDto? = null
)