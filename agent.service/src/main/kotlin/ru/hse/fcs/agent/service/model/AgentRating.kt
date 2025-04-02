package ru.hse.fcs.agent.service.model

import jakarta.persistence.*
import org.springframework.data.annotation.Id
import java.math.BigDecimal
import java.util.UUID

@Entity
@Table(name = "agent_rating")
class AgentRating(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    val id: UUID? = null,

    @OneToOne(cascade = [CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REMOVE])
    @JoinColumn(name = "agent_id", referencedColumnName = "id", unique = true)
    val agent: AgentProfile? = null,

    @Column(name = "grades_sum")
    var gradesSum: Long,

    @Column(name = "grades_number")
    var gradesNumber: Long
) {
    @Column(name = "average_grade")
    var averageGrade: BigDecimal = calcAverageGrade()

    fun addGrade(grade: Long) {
        gradesSum += grade
        gradesNumber += 1
        averageGrade = calcAverageGrade()
    }

    private fun calcAverageGrade() = gradesSum.toBigDecimal() / gradesNumber.toBigDecimal()

    companion object {

        fun createInitRating() = AgentRating(
            gradesSum = 0L,
            gradesNumber = 0L
        )
    }
}