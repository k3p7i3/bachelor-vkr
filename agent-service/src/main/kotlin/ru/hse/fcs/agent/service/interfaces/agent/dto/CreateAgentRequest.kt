package ru.hse.fcs.agent.service.interfaces.agent.dto

import ru.hse.fcs.agent.service.application.dto.UserRegister

data class CreateAgentRequest(
    val agentProfile: AgentProfileDto,
    val userData: UserRegister
)