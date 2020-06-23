package com.whammy.user.service

import com.whammy.user.domain.Authentication
import com.whammy.user.domain.User
import com.whammy.user.exception.AuthenticationFailureException
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UserServiceTest {

    private val userService = UserService()

    @AfterEach
    fun tearDown() {
        userService.refreshCurrentUser()
    }

    @Test
    internal fun testGetCurrentUser() {
        val user = User("sample@example.com", "password", "token")
        userService.setUser(user)
        assertEquals(user, userService.getCurrentUser())
    }

    @Test
    internal fun testGetCurrentUserThrowExceptionIfUserIsNotSet() {
        assertThrows<AuthenticationFailureException> { userService.getCurrentUser() }
    }

    @Test
    internal fun testGetThreadLocalizedUser() {
        val user = User("sample@example.com", "password", "token")
        userService.setUser(user)
        Thread{
            val threadLocalUser = User("thread@example.com", "password", "token")
            userService.setUser(threadLocalUser)
        }
        assertEquals(user, userService.getCurrentUser())
    }
//
//    @Test
//    internal fun testAuthenticateThrowExceptionWhenFailedInAuthentication() {
//        assertThrows<AuthenticationFailureException> { userService.authenticate(Authentication.of("This will fail")) }
//    }
}