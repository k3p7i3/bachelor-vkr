package ru.hse.fcs.user.service.interfaces.dto

data class UpdateUserInfoRequest(
    val username: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null
)