package com.whammy.user.domain

import java.lang.IllegalArgumentException
import java.lang.IndexOutOfBoundsException

data class Authentication (val type: String, val token: String) {

    companion object {
        fun of(authenticationHeaderContent: String) : Authentication {
            try {
                val (type, token) = authenticationHeaderContent.split(" ")
                return Authentication(type, token)
            } catch (e: IndexOutOfBoundsException) {
                throw IllegalArgumentException("You cannot pass non-splittable header content.")
            }
        }
    }
}