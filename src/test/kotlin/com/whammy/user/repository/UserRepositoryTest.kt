package com.whammy.user.repository

import com.whammy.user.domain.User
import com.whammy.user.driver.UserModel
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class UserRepositoryTest {

    private val driver = mockk<UserDriver>()

    private val repository = UserRepository(driver)

    @Test
    fun testIsUserExistsReturnTrueWhenUserModelIsReturned() {
        val email = "aaa@example.com"
        val password = "password"
        every { driver.findUserByEmailAddressAndPassword(email, password) } returns UserModel(
            email,
            password,
            "token1"
        )

        assertTrue { repository.isUserExists(email, password) }

        verify { driver.findUserByEmailAddressAndPassword(email, password) }
    }

    @Test
    fun testIsUserExistsReturnFalseWhenNullIsReturned() {
        val email = "aaa@example.com"
        val password = "password"
        every { driver.findUserByEmailAddressAndPassword(email, password) } returns null

        assertFalse { repository.isUserExists(email, password) }

        verify { driver.findUserByEmailAddressAndPassword(email, password) }
    }

    @Test
    fun testFindUserByEmailAddress() {
        val email = "aaa@example.com"
        val password = "password"
        val token = "token1"
        val userModel = UserModel(email, password, token)
        val user = User(email, password, token)

        every { driver.findUserByEmailAddress(email) } returns userModel

        assertEquals(user, repository.findUserByEmailAddress(email))

        verify { driver.findUserByEmailAddress(email) }
    }

    @Test
    fun testSave() {
        val email = "aaa@example.com"
        val password = "password"
        val token = "token1"
        val userModel = UserModel(email, password, token)
        val user = User(email, password, token)

        every { driver.save(userModel) } just Runs

        repository.save(user)

        verify { driver.save(userModel) }
    }
}