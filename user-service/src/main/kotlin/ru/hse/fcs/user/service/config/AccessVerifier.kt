package ru.hse.fcs.user.service.config

import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class AccessVerifier {

    fun hasAccessToUser(authentication: Authentication, username: String): Boolean {

        return authentication.name == username
    }

    fun hasAccessToAgent(authentication: Authentication, agentId: UUID): Boolean {
        return (
            authentication.authorities.any { it.authority == "AGENT_$agentId" }
                    && authentication.authorities
                .any { it.authority == "AGENT_EMPLOYEE" || it.authority == "AGENT_ADMIN" }
            )
    }
}