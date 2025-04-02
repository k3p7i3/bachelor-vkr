package ru.hse.fcs.agent.service.model.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.hse.fcs.agent.service.model.AgentProfile
import java.util.UUID

interface AgentProfileRepository : JpaRepository<AgentProfile, UUID> {
}