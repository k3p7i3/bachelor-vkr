package ru.hse.fcs.auth.service.config.auth

import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer
import org.springframework.stereotype.Component
import ru.hse.fcs.auth.service.domain.User

@Component
class JwtTokenCustomizer(
    private val userService: UserDetailsService,
): OAuth2TokenCustomizer<JwtEncodingContext> {

    override fun customize(context: JwtEncodingContext) {
        if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
            context.getPrincipal<Authentication>()?.let { principal ->
                val additionalInfo = when (context.authorizationGrantType) {
                    AuthorizationGrantType.CLIENT_CREDENTIALS -> hashMapOf("role" to "extension")
                    else -> {
                        val userDetails = userService.loadUserByUsername(principal.name) as User
                        hashMapOf(
                            "role" to userDetails.authorities.first().authority,
                            "agentId" to userDetails.agentId?.toString()
                        )
                    }
                }

                context.claims.claims { claims ->
                    claims["additionalInfo"] = additionalInfo
                }
            }
        }
    }
}