package com.whammy

import io.micronaut.runtime.Micronaut

object RealworldApplication {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
                .packages("com.whammy")
                .mainClass(RealworldApplication.javaClass)
                .start()
    }
}