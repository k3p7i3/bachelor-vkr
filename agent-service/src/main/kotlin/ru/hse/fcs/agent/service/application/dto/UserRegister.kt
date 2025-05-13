package ru.hse.fcs.agent.service.application.dto

import java.util.*

data class  UserRegister(
    val firstName: String,
    val lastName: String? = null,
    val email: String,
    val password: String,
    val role: UserDto.Role = UserDto.Role.AGENT_ADMIN,
    var agentId: UUID? = null
)