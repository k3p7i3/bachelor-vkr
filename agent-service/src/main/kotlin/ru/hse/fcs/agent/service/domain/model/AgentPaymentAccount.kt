package ru.hse.fcs.agent.service.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.UUID

@Table("payment_account")
class AgentPaymentAccount(
    @Id
    @Column("id")
    private var id: UUID? = null,

    @Column("agent_id")
    val agentId: UUID,

    @Column("bank_card_token")
    val bankCardToken: String,

    @Column("bank_card_first_6")
    val bankCardFirst6: String,

    @Column("bank_card_last_4")
    val bankCardLast4: String,

    @Column("issuer_name")
    val issuerName: String,

    @Column("issuer_country")
    val issuerCountry: String,

    @Column("card_type")
    val cardType: String
) : Persistable<UUID> {
    override fun getId(): UUID? = id

    override fun isNew() = (id == null)
}