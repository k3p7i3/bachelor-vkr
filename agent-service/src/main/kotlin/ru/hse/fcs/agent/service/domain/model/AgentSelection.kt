package ru.hse.fcs.agent.service.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.UUID

@Table("agent_selection")
data class AgentSelection(
    @Id
    private var id: UUID? = null,

    @Column("user_id")
    var userId: UUID,

    @Column("agent_id")
    var agentId: UUID
): Persistable<UUID> {
    override fun getId(): UUID = userId

    override fun isNew() = (id == null)
}