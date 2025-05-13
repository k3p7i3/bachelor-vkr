package ru.hse.fcs.agent.service.interfaces.agent.dto.converter

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.hse.fcs.agent.service.domain.model.AgentProfile
import ru.hse.fcs.agent.service.interfaces.agent.dto.AgentProfileDto

@Component
class AgentProfileDtoToAgentProfileConverter: Converter<AgentProfileDto, AgentProfile> {
    override fun convert(source: AgentProfileDto): AgentProfile =
        AgentProfile(
            id = source.id,
            name = source.name!!,
            description = source.description,
            contactPhoneNumber = source.contactPhoneNumber,
            contactEmail = source.contactEmail,
            legalName = source.legalName
        )
}