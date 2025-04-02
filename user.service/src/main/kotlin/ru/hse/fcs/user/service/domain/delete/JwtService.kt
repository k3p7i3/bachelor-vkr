package ru.hse.fcs.user.service.domain.delete

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.hse.fcs.user.service.domain.User
import java.security.Key
import java.util.Date

@Service
class JwtService(
    @Value("\${security.jwt.secret-key}")
    secretKeyStr: String,

    @Value("\${security.jwt.access.expiration-time}") // in seconds
    private val jwtAccessTokenExpiration: Long,
) {
    private val secretKey = getSecretKey(secretKeyStr)

    fun generateToken(user: User): String =
        Jwts.builder()
            .setSubject(user.username)
            .addClaims(
                mapOf(
                    "role" to user.role.name,
                    "agentId" to user.agentId
                )
            )
            .setIssuedAt(Date())
            .setExpiration(
                Date(
                    System.currentTimeMillis() +
                        SEC_TO_MILLIS_MULT * jwtAccessTokenExpiration
                )
            )
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()


    fun extractClaims(token: String) =
        Jwts
            .parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body

    fun extractUsername(token: String?): String {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody()
            .getSubject()
    }

    private fun getSecretKey(secretKeyStr: String): Key {
        val keyBytes = Decoders.BASE64.decode(secretKeyStr)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    companion object {
        const val SEC_TO_MILLIS_MULT = 1000L;
    }
}