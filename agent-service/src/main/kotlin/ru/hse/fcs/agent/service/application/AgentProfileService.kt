package ru.hse.fcs.agent.service.application

import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.hse.fcs.agent.service.application.dto.UserDto
import ru.hse.fcs.agent.service.application.dto.UserRegister
import ru.hse.fcs.agent.service.domain.model.*
import ru.hse.fcs.agent.service.domain.repository.AgentProfileRepository
import ru.hse.fcs.agent.service.domain.repository.AgentRatingRepository
import ru.hse.fcs.agent.service.infrastructure.AgentImageRepository
import ru.hse.fcs.agent.service.infrastructure.UserClient
import java.math.BigDecimal
import java.util.*

@Service
class AgentProfileService(
    val agentProfileRepository: AgentProfileRepository,
    val agentRatingRepository: AgentRatingRepository,
    val agentImageRepository: AgentImageRepository,
    val userClient: UserClient,
    @Value("\${recommendations-new-agent-score}")
    private val newAgentRecommendationScore: BigDecimal
) {
    @Transactional
    fun createAgent(
        agentProfile: AgentProfile,
        user: UserRegister
    ): Pair<Agent, UserDto?> {
        val agent = agentProfileRepository.save(agentProfile)

        val agentRating = AgentRating.createInitRating(
            agentId = agent.id!!,
            score = newAgentRecommendationScore
        )
        val rating = agentRatingRepository.save(agentRating)
        agentImageRepository.createDirectoryForAgent(agentId = agent.id!!)

        val userData = userClient.registerClient(user.apply { agentId = agent.id!! })

        return Agent(
            profile = agent,
            rating = rating
        ) to userData
    }

    fun updateAgentProfile(agentProfile: AgentProfile): AgentProfile {
        return agentProfileRepository.save(agentProfile)
    }

    fun getAgentProfile(agentId: UUID): AgentProfile {
        return agentProfileRepository.findById(agentId).get()
    }

    fun getAgent(agentId: UUID): Agent {
        val agentProfile = agentProfileRepository.findById(agentId)
        if (agentProfile.isEmpty) {
            throw RuntimeException("Agent(id=$agentId) does not exist")
        }

        val agentRating = agentRatingRepository.findByAgentId(agentId)
        val agentImages = agentImageRepository.getAgentImages(agentId)
        val (avatar, images) = agentImages.partition { it.avatar }
        return Agent(
            profile = agentProfile.get(),
            rating = agentRating.get(),
            avatar = avatar.firstOrNull(),
            images = images.sortedBy { it.createdAt }
        )
    }

    fun getBriefAgent(agentId: UUID): AgentBriefInfo {
        val agentProfile = agentProfileRepository.findById(agentId)
        if (agentProfile.isEmpty) {
            throw RuntimeException("Agent(id=$agentId) does not exist")
        }
        val avatar = agentImageRepository.getAgentAvatar(agentId)
        return AgentBriefInfo(
            agentId = agentId,
            name = agentProfile.get().name,
            avatar = avatar
        )
    }

    fun getAgents(pageNumber: Int, pageSize: Int): Page<Agent> {
        val agentRatings = agentRatingRepository.findAll(
            PageRequest.of(
                pageNumber,
                pageSize,
                Sort.by("recommendationScore")
            )
        )

        val agentIds = agentRatings.map { it.agentId }
        val ratings = agentRatings.associateBy { it.agentId }
        val agentProfiles = agentProfileRepository.findAllByIdIn(agentIds.toList())
            .associateBy { it.id }
        val agentImages = agentImageRepository.getImagesForAgents(agentIds.toList())
            .groupBy { it.agentId }

        val agents = agentIds.map { id ->
            val profile = agentProfiles[id]!!
            val rating = ratings[id]!!
            val (avatar, images) = (agentImages[id] ?: emptyList())
                .partition { it.avatar }

            Agent(
                profile = profile,
                rating = rating,
                avatar = avatar.firstOrNull(),
                images = images.sortedBy { it.createdAt }
            )
        }
        return agents
    }
}