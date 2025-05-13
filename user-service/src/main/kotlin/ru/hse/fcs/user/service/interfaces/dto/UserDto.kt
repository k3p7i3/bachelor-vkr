package ru.hse.fcs.user.service.interfaces.dto

import ru.hse.fcs.user.service.domain.User
import java.util.UUID

data class UserDto(
    val id: UUID,
    val firstName: String,
    val lastName: String? = null,
    val email: String,
    val role: User.Role,
    val agentId: UUID? = null
) {

    companion object {
        fun from(user: User) =
            UserDto(
                id = user.id!!,
                firstName = user.firstName,
                lastName = user.lastName,
                email = user.email,
                role = user.role,
                agentId = user.agentId
            )
    }
}