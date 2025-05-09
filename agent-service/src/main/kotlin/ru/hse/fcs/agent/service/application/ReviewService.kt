package ru.hse.fcs.agent.service.application

import org.springframework.stereotype.Component
import ru.hse.fcs.agent.service.domain.model.Review
import ru.hse.fcs.agent.service.domain.repository.AgentRatingRepository
import ru.hse.fcs.agent.service.domain.repository.ReviewRepository

@Component
class ReviewService(
    val agentRatingRepository: AgentRatingRepository,
    val reviewRepository: ReviewRepository
) {

    fun createReview(review: Review): Review {
        val rating = agentRatingRepository.findByAgentId(review.agentId)
        if (rating.isEmpty) {
            throw RuntimeException("Agent with id=${review.agentId} wasn't found")
        }
        val agentRating = rating.get()
        agentRatingRepository.save(
            agentRating.apply {
                agentRating.addGrade(review.grade)
            }
        )

        val savedReview = reviewRepository.save(review)
        return savedReview
    }
}