package com.avanzo.avanzotestsimulator.network.service

import com.avanzo.avanzotestsimulator.network.dto.login.LoginRequest
import com.avanzo.avanzotestsimulator.network.dto.login.LoginResponse
import com.avanzo.avanzotestsimulator.network.dto.register.RegisterRequest
import com.avanzo.avanzotestsimulator.network.dto.register.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("auth/login")
    suspend fun login(@Body credentials: LoginRequest): LoginResponse

    @POST("auth/register")
    suspend fun register(@Body user: RegisterRequest): RegisterResponse

}