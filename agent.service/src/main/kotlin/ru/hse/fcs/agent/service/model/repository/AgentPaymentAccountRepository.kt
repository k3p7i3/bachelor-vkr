package ru.hse.fcs.agent.service.model.repository

import org.springframework.data.repository.CrudRepository
import ru.hse.fcs.agent.service.model.AgentPaymentAccount
import java.util.UUID

interface AgentPaymentAccountRepository : CrudRepository<AgentPaymentAccount, UUID> {
}