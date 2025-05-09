package ru.hse.fcs.product.service.interfaces.config

import org.springframework.core.convert.converter.Converter
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

class TokenAuthenticationConverter : Converter<Jwt, JwtAuthenticationToken> {

    override fun convert(source: Jwt): JwtAuthenticationToken {
        val additionalInfo = source.claims["additionalInfo"] as Map<String, String?>
        val role = additionalInfo["role"]
        val agentId: String? = additionalInfo["agentId"]

        val authToken = JwtAuthenticationToken(
            source,
            listOfNotNull(
                SimpleGrantedAuthority(role),
                agentId?.let { SimpleGrantedAuthority("AGENT_$it") }
            )
        )
        return authToken
    }
}