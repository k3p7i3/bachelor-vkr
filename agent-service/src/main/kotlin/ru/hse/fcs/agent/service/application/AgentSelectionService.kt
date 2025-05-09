package ru.hse.fcs.agent.service.application

import org.springframework.stereotype.Component
import ru.hse.fcs.agent.service.domain.model.AgentBriefInfo
import ru.hse.fcs.agent.service.domain.model.AgentSelection
import ru.hse.fcs.agent.service.domain.repository.AgentProfileRepository
import ru.hse.fcs.agent.service.domain.repository.AgentSelectionRepository
import java.util.*

@Component
class AgentSelectionService(
    private val agentProfileService: AgentProfileService,
    private val agentProfileRepository: AgentProfileRepository,
    private val agentSelectionRepository: AgentSelectionRepository
) {

    fun saveAgentSelection(agentSelection: AgentSelection): AgentSelection {
        val agentExists = agentProfileRepository.existsById(agentSelection.agentId)
        if (!agentExists) {
            throw RuntimeException("Agent with id=${agentSelection.agentId} does not exist")
        }

        val foundSelection = agentSelectionRepository.findByUserId(
            agentSelection.agentId
        )
        val resultSelection = if (foundSelection.isEmpty) {
            agentSelectionRepository.save(agentSelection)
        } else {
            agentSelectionRepository.save(
                foundSelection.get().apply {
                    agentId = agentSelection.agentId
                }
            )
        }
        return resultSelection
    }

    fun getSelectedAgent(userId: UUID): AgentBriefInfo? {
        val agentSelection = agentSelectionRepository.findByUserId(userId)
        if (agentSelection.isEmpty) {
            return null
        }
        val agentBriefInfo = agentProfileService.getBriefAgent(agentSelection.get().agentId)
        return agentBriefInfo
    }

    fun deleteAgentSelection(userId: UUID) {
        agentSelectionRepository.deleteByUserId(userId)
    }
}