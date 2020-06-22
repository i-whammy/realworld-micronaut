package com.whammy.system

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/systems")
class PingController {

    @Get("/ping")
    fun ping() : String {
        return "pong"
    }
}