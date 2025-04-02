package ru.hse.fcs.agent.service.model.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import ru.hse.fcs.agent.service.model.*
import ru.hse.fcs.agent.service.model.repository.AgentPictureRepository
import ru.hse.fcs.agent.service.model.repository.AgentProfileRepository
import ru.hse.fcs.agent.service.model.repository.AgentRatingRepository
import java.util.*

@Service
class AgentProfileService(
    val agentProfileRepository: AgentProfileRepository,
    val agentRatingRepository: AgentRatingRepository,
    val agentPictureRepository: AgentPictureRepository
) {

    fun createAgentProfile(agentProfile: AgentProfile): AgentProfile {
        val agent = agentProfileRepository.save(
            agentProfile.apply {
                rating = AgentRating.createInitRating()
            }
        )

        return agent
    }

    fun updateAgentProfile(agentProfile: AgentProfile): AgentProfile {
        return agentProfileRepository.save(agentProfile)
    }


    fun getAgentProfile(agentId: UUID): AgentProfile {
        return agentProfileRepository.findById(agentId).get()
    }

    fun getAgentProfileWithAvatar(agentId: UUID): AgentWithAvatar {
        val agent = getAgentProfile(agentId)
        val avatar = agentPictureRepository.findAvatarTrueAndAgentIdIn(listOf(agentId)).firstOrNull()

        return AgentWithAvatar(agent, avatar)
    }

    fun getAgents(pageNumber: Int, pageSize: Int): Page<AgentWithAvatar> {
        val agentProfiles = agentProfileRepository.findAll(
            PageRequest.of(
                pageNumber,
                pageSize,
                Sort.by("rating_averageGrade").descending()
            )
        )

        val avatars = getAvatarsForAgents(agentProfiles.content.mapNotNull { it.id })

        return agentProfiles.map {
            AgentWithAvatar(
                agentProfile = it,
                avatar = avatars[it.id]
            )
        }
    }

    private fun getAvatarsForAgents(agentIds: List<UUID>): Map<UUID, AgentPicture> {
        return agentPictureRepository
            .findAvatarTrueAndAgentIdIn(agentIds)
            .associateBy { it.agentId }
    }
}