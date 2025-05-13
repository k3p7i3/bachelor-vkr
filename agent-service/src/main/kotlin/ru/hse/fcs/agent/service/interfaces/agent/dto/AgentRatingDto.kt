package ru.hse.fcs.agent.service.interfaces.agent.dto

import java.math.BigDecimal

data class AgentRatingDto(
    var rating: BigDecimal,
    var reviewsNumber: Long
)