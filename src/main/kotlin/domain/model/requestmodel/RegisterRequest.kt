package com.example.domain.model.requestmodel

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object Users : Table("users") {
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

data class User(
    val emailId: String,
    val name: String,
    val mobileNo: String,
    val password: String
)

fun ResultRow.toUser() = User(
    emailId = this[Users.emailId],
    name = this[Users.name],
    mobileNo = this[Users.mobileNo],
    password = this[Users.password],
)