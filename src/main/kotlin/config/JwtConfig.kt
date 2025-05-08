package com.example.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import java.util.*

object JwtConfig {

    private const val secretKey = "my-secret-key"  // Should be stored securely, e.g., in env variables
    private const val issuer = "my-issuer"
    private const val audience = "my-audience"

    fun generateToken(userId: String): String {
        val expiration = 60 * 60 * 1000  // 1 hour
        val now = Date().time
        val validity = Date(now + expiration)

        return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withSubject(userId)
            .withExpiresAt(validity)
            .sign(Algorithm.HMAC256(secretKey))
    }

    fun validateToken(token: String): JWTPrincipal? {
        return try {
            val decodedJWT = JWT.require(Algorithm.HMAC256(secretKey))
                .withIssuer(issuer)
                .build()
                .verify(token)

            JWTPrincipal(decodedJWT)
        } catch (e: Exception) {
            null
        }
    }

    fun Application.installJwtAuthentication() {
        install(Authentication) {
            jwt("auth-jwt") {
                realm = "Access to the 'my-app'"
                verifier(JWT.require(Algorithm.HMAC256(secretKey)).withIssuer(issuer).build())
                validate { credential ->
                    if (credential.payload.audience.contains(audience)) {
                        JWTPrincipal(credential.payload)
                    } else {
                        null
                    }
                }
            }
        }
    }
}