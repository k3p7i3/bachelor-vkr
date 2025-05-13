package ru.hse.fcs.agent.service.infrastructure

import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import ru.hse.fcs.agent.service.config.properties.S3Properties
import ru.hse.fcs.agent.service.domain.model.AgentImage
import ru.hse.fcs.agent.service.domain.repository.JdbcAgentImageRepository
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.util.*
import javax.imageio.ImageIO

@Component
class AgentImageRepository(
    private val jdbcAgentImageRepository: JdbcAgentImageRepository,
    private val s3ClientAdapter: S3ClientAdapter,
    s3Properties: S3Properties
) {
    private val bucketName = s3Properties.buckets.agentProfileBucket
    private val urlDuration = s3Properties.presignedUrlDuration

    fun getAgentImage(imageId: UUID): AgentImage {
        val image = jdbcAgentImageRepository.findById(imageId)
            .orElseThrow { RuntimeException("AgentImage with id=$imageId wasn't found") }
        return setPresignedUrlForImage(image)
    }

    fun getAgentImages(agentId: UUID): List<AgentImage> {
        val images = jdbcAgentImageRepository.findAllByAgentId(agentId)
        return images.map { setPresignedUrlForImage(it) }
    }

    fun getAgentAvatar(agentId: UUID): AgentImage? {
        val avatar = jdbcAgentImageRepository.findByAgentIdAndAvatarIsTrue(agentId)
        return if (avatar.isEmpty) {
            null
        } else {
            setPresignedUrlForImage(avatar.get())
        }
    }

    fun getImagesForAgents(agentsIds: List<UUID>): List<AgentImage> {
        val images = jdbcAgentImageRepository.findAllByAgentIdIn(agentsIds)
        return images.map { setPresignedUrlForImage(it) }
    }

    fun setPresignedUrlForImage(image: AgentImage): AgentImage {
        val urlResponse = s3ClientAdapter.getPresignedUrl(bucketName, image.s3FilePath)
        if (urlResponse.responseType == S3ClientAdapter.ResponseType.SUCCESS) {
            image.presignedUrl = urlResponse.response
            image.expire = LocalDateTime.now().plusMinutes(urlDuration)
        }
        return image
    }

    fun createDirectoryForAgent(agentId: UUID) {
        val dirName = AgentImage.FILE_PATH_PREFIX + "/$agentId"
        val result = s3ClientAdapter.createDirectory(
            bucketName,
            dirName
        )
    }

    fun addAgentAvatar(
        agentId: UUID,
        image: MultipartFile
    ): AgentImage {
        val agentImage = uploadAgentImage(agentId, image, isAvatar = true)
        return setPresignedUrlForImage(agentImage)
    }

    fun addAgentImage(
        agentId: UUID,
        image: MultipartFile
    ): AgentImage {
        val agentImage = uploadAgentImage(agentId, image)
        return setPresignedUrlForImage(agentImage)
    }

    fun deleteAgentAvatarIfExists(agentId: UUID) {
        val avatar = jdbcAgentImageRepository.findByAgentIdAndAvatarIsTrue(agentId)
        if (avatar.isPresent) {
            deleteAgentImage(avatar.get().id!!)
        }
    }

    fun deleteAgentImage(imageId: UUID) {
        val agentImage = jdbcAgentImageRepository.findById(imageId)
            .orElseThrow { RuntimeException("AgentImage with id=$imageId wasn't found") }

        val deleteFromS3 = s3ClientAdapter.deleteFiles(bucketName, listOf(agentImage.s3FilePath))

        if (deleteFromS3.responseType == S3ClientAdapter.ResponseType.SUCCESS) {
            jdbcAgentImageRepository.deleteById(imageId)
            return;
        }
        throw RuntimeException("Couldn't delete image")
    }

    private fun uploadAgentImage(
        agentId: UUID,
        file: MultipartFile,
        isAvatar: Boolean = false
    ): AgentImage {
        val originalImage = ImageIO.read(file.inputStream)
        val jpgImage = convertToJpg(originalImage)

        val agentImage = AgentImage.createAgentImage(agentId, isAvatar)

        val outputStream = ByteArrayOutputStream()
        ImageIO.write(jpgImage, "jpg", outputStream)
        val bytes: ByteArray = outputStream.toByteArray()

        val result = s3ClientAdapter.uploadFileAsBytes(
            bucketName = bucketName,
            filePath = agentImage.s3FilePath,
            file = bytes
        )

        if (result.responseType == S3ClientAdapter.ResponseType.SUCCESS) {
            return jdbcAgentImageRepository.save(agentImage)
        }
        throw RuntimeException("Couldn't save image in s3")
    }

    private fun convertToJpg(originalImage: BufferedImage): BufferedImage {
        val image = if (originalImage.type != BufferedImage.TYPE_INT_RGB) {
            val img = BufferedImage(
                originalImage.width,
                originalImage.height,
                BufferedImage.TYPE_INT_RGB
            )
            img.createGraphics().drawImage(originalImage, 0, 0, null)
            img
        } else originalImage

        return image
    }
}