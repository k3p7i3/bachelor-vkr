package ru.hse.fcs.agent.service.interfaces.review.dto

import java.time.LocalDateTime
import java.util.*

data class ReviewDto(
    var id: UUID? = null,
    val authorId: UUID,
    val agentId: UUID,
    val reviewDate: LocalDateTime,
    val grade: Int,
    val text: String? = null
)