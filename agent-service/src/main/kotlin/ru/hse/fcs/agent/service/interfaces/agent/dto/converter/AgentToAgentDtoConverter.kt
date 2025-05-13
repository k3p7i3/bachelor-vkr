package ru.hse.fcs.agent.service.interfaces.agent.dto.converter

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.hse.fcs.agent.service.domain.model.Agent
import ru.hse.fcs.agent.service.domain.model.AgentImage
import ru.hse.fcs.agent.service.domain.model.AgentProfile
import ru.hse.fcs.agent.service.domain.model.AgentRating
import ru.hse.fcs.agent.service.interfaces.agent.dto.AgentDto
import ru.hse.fcs.agent.service.interfaces.agent.dto.AgentImageDto
import ru.hse.fcs.agent.service.interfaces.agent.dto.AgentProfileDto
import ru.hse.fcs.agent.service.interfaces.agent.dto.AgentRatingDto

@Component
class AgentToAgentDtoConverter(
    private val profileConverter: Converter<AgentProfile, AgentProfileDto>,
    private val ratingConverter: Converter<AgentRating, AgentRatingDto>,
    private val imageConverter: Converter<AgentImage, AgentImageDto>
): Converter<Agent, AgentDto> {
    override fun convert(source: Agent): AgentDto =
        AgentDto(
            profile = profileConverter.convert(source.profile)!!,
            rating = ratingConverter.convert(source.rating)!!,
            avatar = source.avatar?.let { imageConverter.convert(it) },
            images = source.images.map {
                imageConverter.convert(it)!!
            }
        )
}