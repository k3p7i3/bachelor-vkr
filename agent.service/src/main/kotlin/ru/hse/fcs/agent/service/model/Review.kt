package ru.hse.fcs.agent.service.model

import jakarta.persistence.*
import org.springframework.data.annotation.Id
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "review")
class Review(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    val id: UUID? = null,

    @Column(name = "author_id")
    val authorId: UUID,

    @Column(name = "agentId")
    val agentId: UUID,

    @Column(name = "reviewDate")
    val reviewDate: LocalDate,

    @Column(name = "grade")
    val grade: Int,

    @Column(name = "text")
    val text: String? = null
)