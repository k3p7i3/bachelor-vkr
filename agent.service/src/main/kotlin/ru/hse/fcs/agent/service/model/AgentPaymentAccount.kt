package ru.hse.fcs.agent.service.model

import jakarta.persistence.*
import org.springframework.data.annotation.Id
import java.util.UUID

@Entity
@Table(name = "payment_account")
class AgentPaymentAccount(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    val id: UUID? = null,

    @Column(name = "agent_id")
    val agentId: UUID,

    @Column(name = "bank_card_token")
    val bankCardToken: String,

    @Column(name = "bank_card_first_6")
    val bankCardFirst6: String,

    @Column(name = "bank_card_last_4")
    val bankCardLast4: String,

    @Column(name = "issuer_name")
    val issuerName: String,

    @Column(name = "issuer_country")
    val issuerCountry: String,

    @Column(name = "card_type")
    val cardType: String
)