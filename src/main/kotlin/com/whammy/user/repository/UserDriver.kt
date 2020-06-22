package com.whammy.user.repository

import com.whammy.user.driver.UserModel

interface UserDriver {
    fun findUserByEmailAddress(email: String) : UserModel?
    fun findUserByEmailAddressAndPassword(email: String, password: String): UserModel?
    fun save(user: UserModel)
}