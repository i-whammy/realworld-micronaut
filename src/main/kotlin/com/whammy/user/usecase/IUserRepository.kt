package com.whammy.user.usecase

import com.whammy.user.domain.User

interface IUserRepository {
    fun isUserExists(email: String, password: String): Boolean
    fun findUserByEmailAddress(email: String): User
    fun save(user: User)
}
