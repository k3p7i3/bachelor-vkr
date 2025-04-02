package ru.hse.fcs.agent.service.model

import jakarta.persistence.*
import org.springframework.data.annotation.Id
import java.util.UUID

@Entity
@Table(name = "agent_picture")
class AgentPicture(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    val id: UUID? = null,

    @Column(name = "agent_id")
    val agentId: UUID,

    @Column(name = "s3_file_path")
    val s3FilePath: String,

    @Column(name ="avatar")
    val avatar: Boolean = false
)