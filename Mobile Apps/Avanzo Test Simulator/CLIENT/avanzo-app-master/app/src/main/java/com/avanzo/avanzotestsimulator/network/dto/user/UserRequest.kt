package com.avanzo.avanzotestsimulator.network.dto.user

data class UserRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val points: Float,
)
