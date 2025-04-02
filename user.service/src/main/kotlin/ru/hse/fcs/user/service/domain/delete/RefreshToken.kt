package ru.hse.fcs.user.service.domain.delete

import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Table("refresh_token")
data class RefreshToken(
    @Id
    @Column("token")
    val token: String,

    @Column("user_id")
    val userId: UUID,

    @Column("expire_at")
    val expireAt: LocalDateTime
)  : Persistable<String> {
    override fun getId() = token

    override fun isNew() = true
}