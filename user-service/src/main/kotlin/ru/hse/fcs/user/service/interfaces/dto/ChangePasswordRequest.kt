package ru.hse.fcs.user.service.interfaces.dto

data class ChangePasswordRequest(
    val username: String,
    val oldPassword: String,
    val newPassword: String
)