package ru.hse.fcs.agent.service.infrastructure

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.web.multipart.MultipartFile
import ru.hse.fcs.agent.service.config.properties.S3Properties
import ru.hse.fcs.agent.service.domain.model.AgentImage
import ru.hse.fcs.agent.service.domain.repository.JdbcAgentImageRepository
import java.time.LocalDateTime
import java.util.Optional
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

@ExtendWith(MockitoExtension::class)
class AgentImageRepositoryTest {

    @Mock
    private lateinit var jdbcAgentImageRepository: JdbcAgentImageRepository

    @Mock
    private lateinit var s3ClientAdapter: S3ClientAdapter

    @Mock
    private lateinit var multipartFile: MultipartFile

    private var s3Properties: S3Properties = mock { mock ->
        whenever(mock.buckets)
            .thenReturn(S3Properties.Buckets("bucketName"))
        whenever(mock.presignedUrlDuration)
            .thenReturn(60L)
    }

    private lateinit var agentImageRepository: AgentImageRepository

    private val agentId = UUID.randomUUID()
    private val imageId = UUID.randomUUID()
    private val testAgentImage = AgentImage(
        id = imageId,
        agentId = agentId,
        s3FilePath = "path/to/image.jpg",
        avatar = false,
        createdAt = LocalDateTime.now()
    )

    @BeforeEach
    fun setUp() {
        agentImageRepository = AgentImageRepository(
            jdbcAgentImageRepository,
            s3ClientAdapter,
            s3Properties
        )
    }

    @Test
    fun `getAgentImage should return image with presigned url when found`() {
        val presignedUrl = "http://presigned.url"
        whenever(jdbcAgentImageRepository.findById(imageId))
            .thenReturn(Optional.of(testAgentImage))
        whenever(s3ClientAdapter.getPresignedUrl(any<String>(), any<String>()))
            .thenReturn(
                S3ClientAdapter.Response(
                    S3ClientAdapter.ResponseType.SUCCESS,
                    presignedUrl,
                )
            )

        val result = agentImageRepository.getAgentImage(imageId)
        assertEquals(presignedUrl, result.presignedUrl)
        verify(jdbcAgentImageRepository).findById(imageId)
        verify(s3ClientAdapter).getPresignedUrl(any(), any())
    }

    @Test
    fun `getAgentImage should throw exception when image not found`() {
        whenever(jdbcAgentImageRepository.findById(imageId))
            .thenReturn(Optional.empty())
        assertFailsWith<RuntimeException> {
            agentImageRepository.getAgentImage(imageId)
        }
    }

    @Test
    fun `getAgentImages should return list of images with presigned urls`() {
        val presignedUrl = "http://presigned.url"
        whenever(jdbcAgentImageRepository.findAllByAgentId(agentId))
            .thenReturn(listOf(testAgentImage))
        whenever(s3ClientAdapter.getPresignedUrl(any<String>(), any<String>()))
            .thenReturn(
                S3ClientAdapter.Response(
                    S3ClientAdapter.ResponseType.SUCCESS,
                    presignedUrl
                )
            )

        val result = agentImageRepository.getAgentImages(agentId)
        assertEquals(1, result.size)
        assertEquals(presignedUrl, result[0].presignedUrl)
    }

    @Test
    fun `getAgentAvatar should return null when no avatar exists`() {
        whenever(jdbcAgentImageRepository.findByAgentIdAndAvatarIsTrue(agentId))
            .thenReturn(Optional.empty())
        val result = agentImageRepository.getAgentAvatar(agentId)
        assertNull(result)
    }

    @Test
    fun `getImagesForAgents should return images with presigned urls`() {
        val presignedUrl = "http://presigned.url"
        whenever(jdbcAgentImageRepository.findAllByAgentIdIn(listOf(agentId)))
            .thenReturn(listOf(testAgentImage))
        whenever(s3ClientAdapter.getPresignedUrl(any<String>(), any<String>()))
            .thenReturn(
                S3ClientAdapter.Response(
                    S3ClientAdapter.ResponseType.SUCCESS,
                    presignedUrl
                )
            )
        val result = agentImageRepository.getImagesForAgents(listOf(agentId))
        assertEquals(1, result.size)
        assertEquals(presignedUrl, result[0].presignedUrl)
    }

    @Test
    fun `createDirectoryForAgent should call s3ClientAdapter`() {
        agentImageRepository.createDirectoryForAgent(agentId)
        verify(s3ClientAdapter).createDirectory(any(), any())
    }

    @Test
    fun `deleteAgentAvatarIfExists should delete when avatar exists`() {
        whenever(jdbcAgentImageRepository.findByAgentIdAndAvatarIsTrue(agentId))
            .thenReturn(Optional.of(testAgentImage))
        whenever(jdbcAgentImageRepository.findById(testAgentImage.id!!))
            .thenReturn(Optional.of(testAgentImage))
        whenever(s3ClientAdapter.deleteFiles(any(), any()))
            .thenReturn(
                S3ClientAdapter.Response(
                    S3ClientAdapter.ResponseType.SUCCESS,
                    listOf(testAgentImage.s3FilePath)
                )
            )
        agentImageRepository.deleteAgentAvatarIfExists(agentId)
        verify(jdbcAgentImageRepository).deleteById(imageId)
    }

    @Test
    fun `deleteAgentImage should throw exception when s3 delete fails`() {
        whenever(jdbcAgentImageRepository.findById(imageId))
            .thenReturn(Optional.of(testAgentImage))
        whenever(s3ClientAdapter.deleteFiles(any(), any()))
            .thenReturn(
                S3ClientAdapter.Response(
                    S3ClientAdapter.ResponseType.FAIL,
                    listOf("error text")
                )
            )

        assertFailsWith<RuntimeException> {
            agentImageRepository.deleteAgentImage(imageId)
        }
    }
}