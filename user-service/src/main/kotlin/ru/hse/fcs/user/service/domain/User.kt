package ru.hse.fcs.user.service.domain

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

@Table("users")
data class User(
    @Id
    @Column("id")
    private var id: UUID? = null,

    @Column("first_name")
    var firstName: String,

    @Column("last_name")
    var lastName: String? = null,

    @Column("email")
    var email: String,

    @Column("password")
    private var password: String,

    @Column("role")
    val role: Role = Role.CLIENT,

    @Column("agent_id")
    val agentId: UUID? = null
) : Persistable<UUID>, UserDetails {

    enum class Role  { CLIENT, AGENT_EMPLOYEE, AGENT_ADMIN }

    override fun getId(): UUID? = id

    override fun isNew() = (id == null)

    @Transient
    override fun getAuthorities() =
        listOf(SimpleGrantedAuthority(role.name))

    override fun getPassword() = password

    fun setPassword(value: String) {
        password = value
    }

    @Transient
    override fun getUsername() = email
}