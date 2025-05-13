package ru.hse.fcs.user.service.application

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.HttpClientErrorException
import ru.hse.fcs.user.service.domain.User
import ru.hse.fcs.user.service.domain.UserRepository
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional
    fun signUp(user: User): User {
        val userExists = userRepository.existsByEmail(user.email)
        if (userExists) {
            throw HttpClientErrorException(
                HttpStatus.BAD_REQUEST,
                "User with the email already exists"
            )
        }

        val encodedPassword: String = passwordEncoder.encode(user.password)
        user.password = encodedPassword

        val savedUser = userRepository.save(user);
        logger.info("Registered new user(id=${savedUser.id}, " +
            "email=${savedUser.email}, role=${savedUser.role})")
        return savedUser
    }

    fun getUser(userEmail: String) =
        userRepository.findByEmail(userEmail).orElseThrow {
            HttpClientErrorException(
                HttpStatus.BAD_REQUEST,
                "User does not exist"
            )
        }

    fun getUserById(userId: UUID) =
        userRepository.findById(userId).orElseThrow {
            HttpClientErrorException(
                HttpStatus.BAD_REQUEST,
                "User does not exist"
            )
        }

    fun updateUserInfo(
        username: String,
        firstName: String?,
        lastName: String?,
        email: String?
    ): User {
        val user = userRepository.findByEmail(username).orElseThrow {
            HttpClientErrorException(
                HttpStatus.BAD_REQUEST,
                "User does not exist"
            )
        }

        email?.let {
            if (email != user.email && userRepository.existsByEmail(it)) {
                throw HttpClientErrorException(
                    HttpStatus.BAD_REQUEST,
                    "User with email already exists"
                )
            }
        }

        firstName?.let { user.firstName = firstName }
        lastName?.let { user.lastName = lastName }
        email?.let { user.email = email }

        userRepository.save(user)
        return user
    }

    fun updatePassword(
        username: String,
        oldPassword: String,
        newPassword: String
    ): Boolean {
        val user = userRepository.findByEmail(username).orElseThrow {
            HttpClientErrorException(
                HttpStatus.BAD_REQUEST,
                "User does not exist"
            )
        }

        if (!passwordEncoder.matches(oldPassword, user.password)) {
            throw HttpClientErrorException(
                HttpStatus.BAD_REQUEST,
                "Incorrect old password"
            )
        }

        user.password = passwordEncoder.encode(newPassword)
        userRepository.save(user)
        return true
    }

    companion object {
        private val logger = LoggerFactory.getLogger(UserService::class.java)
    }
}