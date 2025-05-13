package ru.hse.fcs.agent.service.domain.repository

import org.springframework.data.repository.CrudRepository
import ru.hse.fcs.agent.service.domain.model.Review
import java.math.BigDecimal
import java.util.UUID

interface ReviewRepository : CrudRepository<Review, UUID> {
}