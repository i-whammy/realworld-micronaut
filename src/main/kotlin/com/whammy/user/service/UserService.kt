package com.whammy.user.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.whammy.user.domain.User
import com.whammy.user.exception.AuthorizationFailureException
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Singleton

@Singleton
class UserService {
    // It might be better to have this secret in some external configuration file...
    private val issuer = "Real World App"

    private val secret = "VGhpcyBrZXkgbXVzdCBub3QgYmUgZGVjb2RlZA=="

    private val algorithm = Algorithm.HMAC256(secret)

    private val verifier = JWT.require(algorithm).withIssuer(issuer).build()

    fun issueNewToken(user: User): User {
        return User(
            user.email, user.password, JWT.create()
                .withIssuer(issuer)
                .withClaim("userId", user.email)
                .withExpiresAt(Date.from(LocalDateTime.now().plusHours(1L).atZone(ZoneId.systemDefault()).toInstant()))
                .sign(algorithm)
        )
    }

    private fun isValid(authorizationType: String, token: String): Boolean {
        try {
            val decodedJWT = verifier.verify(token)
            return decodedJWT.expiresAt > Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()) && authorizationType == "Token"
        } catch (e: Exception) {
            return false
        }
    }

    fun getUserId(authorizationHeaderContent: String):String {
        val (authorizationType, token) = authorizationHeaderContent.split(" ")
        if (isValid(authorizationType, token)) {
            return verifier.verify(token).getClaim("userId").asString()
        } else {
            throw AuthorizationFailureException("Authorization Failed.")
        }
    }
}
