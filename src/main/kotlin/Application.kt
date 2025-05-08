package com.example

import com.example.config.DatabaseFactory
import com.example.config.JwtConfig.installJwtAuthentication
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    DatabaseFactory.init()
    installJwtAuthentication()

    configureRouting()
}
