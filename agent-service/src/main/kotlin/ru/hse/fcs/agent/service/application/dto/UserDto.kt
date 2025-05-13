package ru.hse.fcs.agent.service.application.dto

import java.util.*

data class UserDto(
    val id: UUID,
    val firstName: String,
    val lastName: String? = null,
    val email: String,
    val role: Role,
    val agentId: UUID? = null
) {
    enum class Role { AGENT_EMPLOYEE, AGENT_ADMIN }
}