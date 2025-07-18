package com.avanzo.avanzotestsimulator.network.dto.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("data") val token: String,
    @SerializedName("message") val message: String
)