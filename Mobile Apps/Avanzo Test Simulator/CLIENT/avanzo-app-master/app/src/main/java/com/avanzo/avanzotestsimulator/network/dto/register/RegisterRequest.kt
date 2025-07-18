package com.avanzo.avanzotestsimulator.network.dto.register

data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val role: String,
    val email: String,
    val password: String,
    val password_confirmation: String
)