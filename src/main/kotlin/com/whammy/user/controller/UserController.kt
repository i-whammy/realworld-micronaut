package com.whammy.user.controller

import com.whammy.user.domain.LoginInformation
import com.whammy.user.usecase.UserUsecase
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import java.util.concurrent.CompletableFuture
import javax.inject.Inject

@Controller("/api/users")
class UserController(@Inject private val userUsecase: UserUsecase) {

    @Post("/login", consumes = [MediaType.APPLICATION_JSON])
    fun login(@Body loginInformation: CompletableFuture<LoginRequest>): CompletableFuture<HttpResponse<LoginResponse>> {
        return loginInformation.thenApply {
            val user = userUsecase.login(LoginInformation(it.user.email, it.user.password))
            HttpResponse.ok(LoginResponse(LoginResponse.UserResponse(user.email, user.token)))
        }
    }

    data class LoginRequest(val user: UserRequest) {
        data class UserRequest(val email: String, val password: String)
    }

    data class LoginResponse(val user: UserResponse) {
        data class UserResponse(val email: String, val token: String)
    }
}