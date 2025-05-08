package com.example.domain.model.requestmodel

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val emailId: String,
    val password: String
)

@Serializable
data class TokenResponse(val token: String)
