package com.whammy.user.exception

import java.lang.Exception

class LoginFailureException(override val message: String): Exception()

class AuthorizationFailureException(override val message: String): Exception()