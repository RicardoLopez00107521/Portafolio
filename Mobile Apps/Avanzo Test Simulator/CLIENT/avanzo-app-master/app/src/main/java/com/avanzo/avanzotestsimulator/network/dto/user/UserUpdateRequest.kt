package com.avanzo.avanzotestsimulator.network.dto.user

data class UserUpdateRequest(
    val newPassword: String,
    val confirmPassword: String,
)
