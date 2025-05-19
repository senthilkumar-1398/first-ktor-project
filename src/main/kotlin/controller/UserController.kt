package com.example.controller

import com.example.config.JwtConfig
import com.example.domain.model.requestmodel.LoginRequest
import com.example.domain.model.requestmodel.RegisterRequest
import com.example.domain.model.requestmodel.User
import com.example.service.UserService
import com.example.utils.ApiResponse
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.thymeleaf.*

fun Route.userController(userService: UserService) {
    get("/register") {
        call.respond(ThymeleafContent("register", mapOf()))
    }

    get("/login") {
        call.respond(ThymeleafContent("login", mapOf()))
    }

    // ✅ API Endpoint
    route("/api") {
        post("/create-user") {
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
            println("➡️ /api/get-all-users called")
            try {
                val users = userService.getUsers()
                println("✅ Users fetched successfully: $users")

                call.respond(
                    HttpStatusCode.OK,
                    ApiResponse(status = true, message = "Users fetched", data = users)
                )
            } catch (e: Exception) {
                println("❌ Error in /api/get-all-users: ${e.message}")
                e.printStackTrace()

                call.respond(
                    HttpStatusCode.InternalServerError,
                    ApiResponse<Unit>(status = false, message = "Something went wrong: ${e.message}", data = null)
                )
            }
        }

        put("/users/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse<Unit>(status = false, message = "Invalid ID")
                )
                return@put
            }

            try {
                val user = call.receive<User>()
                val updated = userService.updateUser(id, user)
                if (updated) {
                    call.respond(
                        HttpStatusCode.OK,
                        ApiResponse<Unit>(status = true, message = "User updated successfully")
                    )
                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        ApiResponse<Unit>(status = false, message = "User not found")
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse<Unit>(status = false, message = "Invalid input: ${e.message}")
                )
            }
        }

        delete("/users/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse<Unit>(status = false, message = "Invalid ID")
                )
                return@delete
            }

            try {
                val deleted = userService.deleteUser(id)
                if (deleted) {
                    call.respond(
                        HttpStatusCode.OK,
                        ApiResponse<Unit>(status = true, message = "User deleted successfully")
                    )
                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        ApiResponse<Unit>(status = false, message = "User not found")
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ApiResponse<Unit>(status = false, message = "Error: ${e.message}")
                )
            }
        }
    }
}