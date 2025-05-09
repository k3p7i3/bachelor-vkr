package ru.hse.fcs.agent.service.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.util.UUID

@Table("agent_rating")
class AgentRating(
    @Id
    @Column("id")
    private var id: UUID? = null,

    @Column("agent_id")
    val agentId: UUID,

    @Column("grades_sum")
    var gradesSum: Long,

    @Column("reviews_number")
    var reviewsNumber: Long,

    @Column("recommendation_score")
    var recommendationScore: BigDecimal? = null
): Persistable<UUID> {
    @Column("average_grade")
    var rating: BigDecimal = calcAverageGrade()

    override fun getId(): UUID? = id

    override fun isNew() = (id == null)

    fun addGrade(grade: Int) {
        gradesSum += grade
        reviewsNumber += 1
        rating = calcAverageGrade()
    }

    private fun calcAverageGrade() = if (reviewsNumber == 0L) {
        BigDecimal.ZERO
    } else {
        gradesSum.toBigDecimal() / reviewsNumber.toBigDecimal()
    }

    companion object {
        fun createInitRating(
            agentId: UUID,
            score: BigDecimal
        ): AgentRating {
            val rating = AgentRating(
                agentId = agentId,
                gradesSum = 0L,
                reviewsNumber = 0L,
                recommendationScore = score
            )
            return rating
        }
    }
}