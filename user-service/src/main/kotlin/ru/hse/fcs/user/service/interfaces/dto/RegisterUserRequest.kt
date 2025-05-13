package ru.hse.fcs.user.service.interfaces.dto

import ru.hse.fcs.user.service.domain.User
import java.util.*

data class RegisterUserRequest(
    val firstName: String,
    val lastName: String? = null,
    val email: String,
    val password: String,
    val role: User.Role,
    val agentId: UUID? = null
)