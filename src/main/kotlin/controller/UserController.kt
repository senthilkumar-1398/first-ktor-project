package com.example.controller

import com.example.config.JwtConfig
import com.example.domain.model.requestmodel.LoginRequest
import com.example.domain.model.requestmodel.RegisterRequest
import com.example.service.UserService
import com.example.utils.ApiResponse
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.thymeleaf.*

fun Route.userController(userService: UserService) {
    get("/register") {
        call.respondText(
            this::class.java.classLoader.getResource("register.html")!!.readText(),
            ContentType.Text.Html
        )
    }

    get("/login") {
        call.respond(ThymeleafContent("login", mapOf()))
    }

    post("/register") {
        try {
            val params = call.receiveParameters()

            val registerRequest = RegisterRequest(
                name = params["name"] ?: "",
                emailId = params["emailId"] ?: "",
                mobileNo = params["mobileNo"] ?: "",
                password = params["password"] ?: ""
            )

            userService.createUser(registerRequest)

            call.respond(
                HttpStatusCode.Created,
                ApiResponse<Unit>(status = true, message = "User added", data = null)
            )

        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(
                HttpStatusCode.BadRequest,
                ApiResponse<Unit>(status = false, message = "Invalid input", data = null)
            )
        }
    }

    post("/login") {
        try {

            val params = call.receiveParameters()

            val loginRequest = LoginRequest(
                emailId = params["emailId"] ?: "",
                password = params["password"] ?: ""
            )
            val user = userService.authenticateUser(loginRequest.emailId, loginRequest.password)
            println("Authentication failed for email: ${user}")

            if (user != null) {
                println("Authentication failed for email: ${11111}")

                val token = JwtConfig.generateToken(user.toString())

//                call.respond(
//                    HttpStatusCode.OK,
//                    ApiResponse(status = true, message = "Login successful", data = TokenResponse(token))
//                )
                call.respond(ThymeleafContent("home", mapOf("user" to user)))
            } else {
                println("Authentication failed for email: ${222222}")
                call.respond(
                    HttpStatusCode.Unauthorized,
                    ApiResponse<Unit>(status = false, message = "Invalid credentials", data = null)
                )

            }
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(
                HttpStatusCode.InternalServerError,
                ApiResponse<Unit>(status = false, message = "Something went wrong", data = null)
            )
        }
    }

    get("/get-all-users") {
        try {
            val users = userService.getUsers()
            call.respond(
                HttpStatusCode.OK,
                ApiResponse(status = true, message = "Users fetched", data = users)
            )
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                ApiResponse<Unit>(status = false, message = "Something went wrong", data = null)
            )
        }
    }

}