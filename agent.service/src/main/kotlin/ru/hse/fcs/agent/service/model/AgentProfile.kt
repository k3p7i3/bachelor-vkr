package ru.hse.fcs.agent.service.model

import jakarta.persistence.*
import org.springframework.data.annotation.Id
import java.util.UUID

@Entity
@Table(name = "agent_profile")
class AgentProfile(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    val id: UUID? = null,

    @Column(name = "name")
    val name: String,

    @Column(name = "description")
    val description: String,

    @Column(name = "phone_number")
    val contactPhoneNumber: String,

    @Column(name = "email")
    val contactEmail: String,

    @Column(name = "legal_name")
    val legalName: String,

    @OneToOne(
        mappedBy = "agent",
        cascade = [CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REMOVE]
    )
    var rating: AgentRating? = null
)