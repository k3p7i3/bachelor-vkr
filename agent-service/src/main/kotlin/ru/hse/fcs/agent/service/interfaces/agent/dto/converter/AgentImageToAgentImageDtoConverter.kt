package ru.hse.fcs.agent.service.interfaces.agent.dto.converter

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.hse.fcs.agent.service.domain.model.AgentImage
import ru.hse.fcs.agent.service.interfaces.agent.dto.AgentImageDto

@Component
class AgentImageToAgentImageDtoConverter: Converter<AgentImage, AgentImageDto> {
    override fun convert(source: AgentImage): AgentImageDto =
        AgentImageDto(
            id = source.id!!,
            agentId = source.agentId,
            presignedUrl = source.presignedUrl,
            expire = source.expire,
            createdAt = source.createdAt
        )
}