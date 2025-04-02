package ru.hse.fcs.user.service.domain.delete

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.hse.fcs.user.service.domain.UserRepository
import java.time.LocalDateTime
import java.util.UUID

@Service
class RefreshTokenService(
    @Value("\${security.jwt.refresh.expiration-time}")
    val jwtRefreshTokenExpiration: Long = 60000, // in seconds
    val refreshTokenRepository: RefreshTokenRepository,
    val userRepository: UserRepository
) {

    fun createRefreshToken(userEmail: String): RefreshToken {
        val user = userRepository.findByEmail(userEmail)

        val refreshToken = RefreshToken(
            token = UUID.randomUUID().toString(),
            expireAt = LocalDateTime.now().plusSeconds(jwtRefreshTokenExpiration),
            userId = user?.id!!
        )
        return refreshTokenRepository.save(refreshToken)
    }

    fun findByToken(token: String): RefreshToken {
        return refreshTokenRepository.findByToken(token)
    }

    fun verifyExpiration(token: RefreshToken): RefreshToken {
        if (token.expireAt < LocalDateTime.now()) {
            refreshTokenRepository.delete(token)
            throw RuntimeException(token.token + " Refresh token is expired.")
        }
        return token
    }
}