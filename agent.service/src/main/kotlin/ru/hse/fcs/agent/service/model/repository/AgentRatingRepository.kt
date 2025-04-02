package ru.hse.fcs.agent.service.model.repository

import org.springframework.data.repository.CrudRepository
import ru.hse.fcs.agent.service.model.AgentRating
import java.util.UUID

interface AgentRatingRepository : CrudRepository<AgentRating, UUID> {
}