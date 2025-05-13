package ru.hse.fcs.agent.service.interfaces.review.dto

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.hse.fcs.agent.service.domain.model.Review

@Component
class ReviewToReviewDtoConverter: Converter<Review, ReviewDto> {
    override fun convert(source: Review): ReviewDto? =
        ReviewDto(
            id = source.id,
            authorId = source.authorId,
            agentId = source.agentId,
            reviewDate = source.reviewDate,
            grade = source.grade,
            text = source.text
        )
}