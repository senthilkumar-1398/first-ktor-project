package com.example.domain.model.requestmodel

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow

object Users : IntIdTable("users") {
    val emailId = varchar("emailId", 255)
    val name = varchar("name", 255)
    val mobileNo = varchar("mobileNo", 15)
    val password = varchar("password", 255)
}

@Serializable
data class RegisterRequest(
    val emailId: String,
    val name: String,
    val mobileNo: String,
    val password: String
)

@Serializable
data class User(
    val id: Int? = null,
    val emailId: String,
    val name: String,
    val mobileNo: String,
    val password: String? = null
)

fun ResultRow.toUser() = User(
    id = this[Users.id].value,
    emailId = this[Users.emailId],
    name = this[Users.name],
    mobileNo = this[Users.mobileNo],
    password = this[Users.password],
)