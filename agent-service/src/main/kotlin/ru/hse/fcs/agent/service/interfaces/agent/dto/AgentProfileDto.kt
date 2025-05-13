package ru.hse.fcs.agent.service.interfaces.agent.dto

import java.util.*

data class AgentProfileDto(
    var id: UUID? = null,
    var name: String? = null,
    var description: String? = null,
    var contactPhoneNumber: String? = null,
    var contactEmail: String? = null,
    var legalName: String? = null
)