package com.example.repository

import com.example.domain.model.requestmodel.RegisterRequest
import com.example.domain.model.requestmodel.User
import com.example.domain.model.requestmodel.Users
import com.example.domain.model.requestmodel.toUser
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class UserRepository {

    fun addUser(registerRequest: RegisterRequest): User = transaction {
        Users.insert {
            it[emailId] = registerRequest.emailId
            it[name] = registerRequest.name
            it[mobileNo] = registerRequest.mobileNo
            it[password] = registerRequest.password
        }

        User(
            emailId = registerRequest.emailId,
            name = registerRequest.name,
            mobileNo = registerRequest.mobileNo,
            password = ""
        )
    }

    fun getUserByEmail(email: String): User? = transaction {
        Users.select { Users.emailId eq email }
            .map { it.toUser() }
            .singleOrNull()
    }

    fun getAllUsers(): List<User> = transaction {
        Users.selectAll().map { it.toUser() }
    }

//    fun updateUser(id: Int, user: User): Boolean = transaction {
//        Users.update({ Users.emailId eq id }) {
//            it[name] = user.name
//            it[email] = user.email
//        } > 0
//    }
//
//    fun deleteUser(id: Int): Unit = transaction {
//        val deletedRows = Users.deleteWhere { Users.id eq id }
//        deletedRows > 0
//    }


}
