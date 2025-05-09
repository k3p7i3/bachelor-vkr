package ru.hse.fcs.agent.service.interfaces.review

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.hse.fcs.agent.service.application.ReviewService
import ru.hse.fcs.agent.service.interfaces.review.dto.ReviewDto
import ru.hse.fcs.agent.service.interfaces.review.dto.ReviewDtoToReviewConverter
import ru.hse.fcs.agent.service.interfaces.review.dto.ReviewToReviewDtoConverter

@RestController
@RequestMapping("/api/review")
class ReviewController(
    private val reviewToReviewDtoConverter: ReviewToReviewDtoConverter,
    private val reviewDtoToReviewConverter: ReviewDtoToReviewConverter,
    private val reviewService: ReviewService
) {

    @PostMapping
    fun createReview(@RequestBody body: ReviewDto): ResponseEntity<ReviewDto> {
        val review = reviewService.createReview(
            reviewDtoToReviewConverter.convert(body)!!
        )
        return ResponseEntity.ok(
            reviewToReviewDtoConverter.convert(review)
        )
    }
}