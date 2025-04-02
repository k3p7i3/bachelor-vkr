package ru.hse.fcs.user.service.domain

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

@Table("user")
data class User(
    @Id
    @Column("id")
    val id: UUID? = null,

    @Column("first_name")
    val firstName: String,

    @Column("last_name")
    val lastName: String? = null,

    @Column("email")
    val email: String,

    @Column("password")
    val password: String,

    @Column("role")
    val role: Role = Role.CLIENT,

    @Column("agent_id")
    val agentId: UUID? = null
) : Persistable<UUID>, UserDetails {

    enum class Role  { CLIENT, AGENT_EMPLOYEE, AGENT_ADMIN }

    override fun getId() = id

    override fun isNew() = (id == null)

    @Transient
    override fun getAuthorities() =
        listOf(SimpleGrantedAuthority(role.name))

    @Transient
    override fun getPassword() = password

    @Transient
    override fun getUsername() = email
}