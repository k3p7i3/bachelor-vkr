package ru.hse.fcs.agent.service.interfaces.agent.dto.converter

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.hse.fcs.agent.service.domain.model.AgentRating
import ru.hse.fcs.agent.service.interfaces.agent.dto.AgentRatingDto

@Component
class AgentRatingToAgentRatingDtoConverter: Converter<AgentRating, AgentRatingDto> {
    override fun convert(source: AgentRating): AgentRatingDto =
        AgentRatingDto(
            rating = source.rating,
            reviewsNumber = source.reviewsNumber
        )
}