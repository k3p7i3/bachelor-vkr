package ru.hse.fcs.agent.service.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID


@Table("agent_image")
class AgentImage(
    @Id
    @Column("id")
    private var id: UUID? = null,

    @Column("agent_id")
    val agentId: UUID,

    @Column("s3_file_path")
    val s3FilePath: String,

    @Column("created_at")
    val createdAt: LocalDateTime,

    @Column("avatar")
    val avatar: Boolean = false
) : Persistable<UUID> {

    @Transient
    var presignedUrl: String? = null

    @Transient
    var expire: LocalDateTime? = null

    override fun getId(): UUID? = id

    override fun isNew() = true

    companion object {
        fun createAgentImage(
            agentId: UUID,
            isAvatar: Boolean = false
        ): AgentImage {
            val uuid = UUID.randomUUID()
            val creationTime = LocalDateTime.now()
            val filePath = "$FILE_PATH_PREFIX/$agentId/${creationTime.format(formatter)}-$uuid.jpg"

            return AgentImage(
                id = uuid,
                agentId = agentId,
                createdAt = creationTime,
                s3FilePath = filePath,
                avatar = isAvatar
            )
        }

        const val FILE_PATH_PREFIX: String = "agent/profile"
        private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm")
    }
}