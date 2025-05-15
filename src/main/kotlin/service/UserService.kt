package com.example.service

import com.example.domain.model.requestmodel.RegisterRequest
import com.example.domain.model.requestmodel.User
import com.example.repository.UserRepository

class UserService(private val repo: UserRepository) {
    fun getUsers(): List<User> = repo.getAllUsers()
    fun createUser(registerRequest: RegisterRequest): User = repo.addUser(registerRequest)
//    fun updateUser(id: Int, user: User): Boolean = repo.updateUser(id, user)
//    fun deleteUser(id: Int): Boolean = repo.deleteUser(id)
    fun authenticateUser(email: String, password: String): User? {
        val user = repo.getUserByEmail(email)
        return if (user != null && user.password == password) {
            println("User authenticated successfully")
            user
        } else {
            null
        }
    }
}
