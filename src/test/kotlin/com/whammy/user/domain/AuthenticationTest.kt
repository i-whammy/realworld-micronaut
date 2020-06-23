package com.whammy.user.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException

class AuthenticationTest {

    @Test
    internal fun testOfCreatesAuthentication() {
        val authentication = Authentication("Token", "token.is.here")
        assertEquals(Authentication.of("Token token.is.here"), authentication)
    }

    @Test
    internal fun testOfThrowExceptionWhenFailedInSplit() {
        assertThrows<IllegalArgumentException> { Authentication.of("ThisIsNotValidContent") }
    }
}