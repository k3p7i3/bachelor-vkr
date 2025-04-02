package ru.hse.fcs.agent.service.model.repository

import org.springframework.data.repository.PagingAndSortingRepository
import ru.hse.fcs.agent.service.model.Review
import java.util.UUID

interface ReviewRepository : PagingAndSortingRepository<Review, UUID> {
}