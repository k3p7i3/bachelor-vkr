package ru.hse.fcs.agent.service.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.UUID

@Table("agent_profile")
class AgentProfile(
    @Id
    @Column("id")
    private var id: UUID? = null,

    @Column("name")
    val name: String,

    @Column("description")
    val description: String? = null,

    @Column("phone_number")
    val contactPhoneNumber: String? = null,

    @Column("email")
    val contactEmail: String? = null,

    @Column("legal_name")
    val legalName: String? = null,
): Persistable<UUID> {
    override fun getId(): UUID? = id

    override fun isNew() = (id == null)
}