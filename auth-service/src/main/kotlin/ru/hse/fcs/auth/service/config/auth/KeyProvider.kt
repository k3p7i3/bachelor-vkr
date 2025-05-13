package ru.hse.fcs.auth.service.config.auth

import com.nimbusds.jose.jwk.RSAKey
import org.springframework.stereotype.Component
import java.security.KeyPairGenerator
import java.security.NoSuchAlgorithmException
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.*

@Component
class KeyProvider {

    fun rsaKey(): RSAKey {
        var generator: KeyPairGenerator?
        try {
            generator = KeyPairGenerator.getInstance("RSA")
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        }
        generator.initialize(2048)
        val keyPair = generator.generateKeyPair()

        val publicKey = keyPair.public as RSAPublicKey
        val privateKey = keyPair.private as RSAPrivateKey

        return RSAKey.Builder(publicKey)
            .privateKey(privateKey)
            .keyID(UUID.randomUUID().toString()).build()
    }
}