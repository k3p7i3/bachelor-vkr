package ru.hse.fcs.auth.service.domain

import org.springframework.data.repository.CrudRepository
import java.util.Optional
import java.util.UUID

interface UserRepository : CrudRepository<User, UUID> {

    fun findByEmail(email: String): Optional<User>
}