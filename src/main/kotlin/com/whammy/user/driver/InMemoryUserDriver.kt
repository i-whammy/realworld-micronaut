package com.whammy.user.driver

import com.whammy.user.repository.UserDriver
import javax.inject.Singleton

@Singleton
class InMemoryUserDriver: UserDriver {
    private val users = mutableListOf(
        UserModel("reader@example.com", "reader", "-"),
        UserModel("writer@example.com", "writer", "-")
        )

    override fun findUserByEmailAddress(email: String): UserModel? {
        return users.find { it.email == email }
    }

    override fun findUserByEmailAddressAndPassword(email: String, password: String): UserModel? {
        return users.find { it.email == email && it.password == password }
    }

    override fun save(user: UserModel) {
        users.removeIf { it.email == user.email }
        users.add(user)
    }
}