package ru.hse.fcs.user.service.interfaces

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.hse.fcs.user.service.application.UserService
import ru.hse.fcs.user.service.config.AccessVerifier
import ru.hse.fcs.user.service.domain.User
import ru.hse.fcs.user.service.interfaces.dto.ChangePasswordRequest
import ru.hse.fcs.user.service.interfaces.dto.RegisterUserRequest
import ru.hse.fcs.user.service.interfaces.dto.UpdateUserInfoRequest
import ru.hse.fcs.user.service.interfaces.dto.UserDto

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService,
    private val accessVerifier: AccessVerifier
) {

    @PostMapping("/register")
    fun registerUser(
        @RequestBody body: RegisterUserRequest
    ): ResponseEntity<UserDto> {
        val user = userService.signUp(
            User(
                firstName = body.firstName,
                lastName = body.lastName,
                email = body.email,
                password = body.password,
                role = body.role,
                agentId = body.agentId
            )
        )

        return ResponseEntity.ok(UserDto.from(user))
    }

    @PreAuthorize("@accessVerifier.hasAccessToUser(authentication, #email)")
    @GetMapping
    fun getUserByEmail(
        @RequestParam email: String
    ): ResponseEntity<UserDto> {
        val user = userService.getUser(email)
        return ResponseEntity.ok(UserDto.from(user))
    }

    @PreAuthorize("@accessVerifier.hasAccessToUser(authentication, #body.username)")
    @PostMapping("/update")
    fun updateUserInfo(
        @RequestBody body: UpdateUserInfoRequest
    ): ResponseEntity<UserDto> {
        val user = userService.updateUserInfo(
            username = body.username,
            firstName = body.firstName,
            lastName = body.lastName,
            email = body.email
        )

        return ResponseEntity.ok(UserDto.from(user))
    }

    @PreAuthorize("@accessVerifier.hasAccessToUser(authentication, #body.username)")
    @PostMapping("/change-password")
    fun changePassword(
        @RequestBody body: ChangePasswordRequest
    ): ResponseEntity<Boolean> {
        val result = userService.updatePassword(
            username = body.username,
            oldPassword = body.oldPassword,
            newPassword = body.newPassword
        )

        return ResponseEntity.ok(result)
    }
}