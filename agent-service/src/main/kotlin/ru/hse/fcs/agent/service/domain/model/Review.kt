package ru.hse.fcs.agent.service.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.UUID

@Table("review")
class Review(
    @Id
    @Column("id")
    private var id: UUID? = null,

    @Column("author_id")
    val authorId: UUID,

    @Column("agentId")
    val agentId: UUID,

    @Column("review_date")
    val reviewDate: LocalDateTime,

    @Column("grade")
    val grade: Int,

    @Column("text")
    val text: String? = null
): Persistable<UUID> {
    override fun getId(): UUID? = id

    override fun isNew() = (id == null)
}