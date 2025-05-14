package ru.hse.fcs.agent.service.application

import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.web.multipart.MultipartFile
import ru.hse.fcs.agent.service.domain.model.AgentImage
import ru.hse.fcs.agent.service.infrastructure.AgentImageRepository
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class AgentImageServiceTest {

    @Mock
    private lateinit var agentImageRepository: AgentImageRepository

    @Mock
    private lateinit var multipartFile: MultipartFile

    @InjectMocks
    private lateinit var agentImageService: AgentImageService

    private val agentId = UUID.randomUUID()
    private val imageId = UUID.randomUUID()
    private val agentImage = AgentImage(
        UUID.randomUUID(),
        agentId,
        "test.jpg",
        LocalDateTime.now(),
        true
    )

    @Test
    fun `addAvatarForAgent should delete existing avatar and add new one`() {
        whenever(
            agentImageRepository.addAgentAvatar(agentId, multipartFile)
        ).thenReturn(agentImage)
        val result = agentImageService.addAvatarForAgent(agentId, multipartFile)
        assertEquals(agentImage, result)
        verify(agentImageRepository).deleteAgentAvatarIfExists(agentId)
        verify(agentImageRepository).addAgentAvatar(agentId, multipartFile)
    }

    @Test
    fun `addAgentImage should add single image`() {
        whenever(agentImageRepository.addAgentImage(agentId, multipartFile)).thenReturn(agentImage)
        val result = agentImageService.addAgentImage(agentId, multipartFile)
        assertEquals(agentImage, result)
        verify(agentImageRepository).addAgentImage(agentId, multipartFile)
    }

    @Test
    fun `addAgentImages should add multiple images`() {
        val images = arrayOf(multipartFile, multipartFile)
        whenever(agentImageRepository.addAgentImage(agentId, multipartFile)).thenReturn(agentImage)
        val result = agentImageService.addAgentImages(agentId, images)
        assertEquals(2, result.size)
        verify(agentImageRepository, times(2))
            .addAgentImage(agentId, multipartFile)
    }

    @Test
    fun `deleteAgentImage should delete image by id`() {
        agentImageService.deleteAgentImage(imageId)
        verify(agentImageRepository).deleteAgentImage(imageId)
    }
}