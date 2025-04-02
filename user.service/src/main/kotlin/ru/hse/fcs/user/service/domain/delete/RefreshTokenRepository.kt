package ru.hse.fcs.user.service.domain.delete

import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface RefreshTokenRepository : CrudRepository<RefreshToken, UUID> {

    fun findByToken(token: String): RefreshToken
}