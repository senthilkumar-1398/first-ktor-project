package com.example

import com.example.controller.userController
import com.example.repository.UserRepository
import com.example.service.UserService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    val userRepo = UserRepository()
    val userService = UserService(userRepo)

    routing {
        userController(userService)
    }
}
