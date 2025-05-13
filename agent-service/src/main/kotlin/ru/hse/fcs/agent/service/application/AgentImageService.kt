package ru.hse.fcs.agent.service.application

import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import ru.hse.fcs.agent.service.domain.model.AgentImage
import ru.hse.fcs.agent.service.infrastructure.AgentImageRepository
import java.util.*

@Component
class AgentImageService(
    private val agentImageRepository: AgentImageRepository
) {
    fun addAvatarForAgent(
        agentId: UUID,
        image: MultipartFile
    ): AgentImage {
        agentImageRepository.deleteAgentAvatarIfExists(agentId)
        val avatar = agentImageRepository.addAgentAvatar(agentId, image)
        return avatar
    }

    fun addAgentImage(
        agentId: UUID,
        image: MultipartFile
    ): AgentImage {
        val agentImage = agentImageRepository.addAgentImage(agentId, image)
        return agentImage
    }

    fun addAgentImages(
        agentId: UUID,
        images: Array<MultipartFile>
    ): List<AgentImage> {
        val agentImages = mutableListOf<AgentImage>()
        images.forEach {
            val img = agentImageRepository.addAgentImage(agentId, it)
            agentImages.add(img)
        }
        return agentImages
    }

    fun deleteAgentImage(imageId: UUID) {
        agentImageRepository.deleteAgentImage(imageId)
    }
}