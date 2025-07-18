package com.avanzo.avanzotestsimulator.network.service

import com.avanzo.avanzotestsimulator.network.dto.user.UserRequest
import com.avanzo.avanzotestsimulator.network.dto.user.UserResponse
import com.avanzo.avanzotestsimulator.network.dto.user.UserUpdateRequest
import com.avanzo.avanzotestsimulator.network.dto.user.UserUpdateResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface UserService {
    @GET("auth/whoami")
    suspend fun getUser(): UserResponse

    @PUT("user/changePass")
    suspend fun updateUser(@Body userInfo: UserUpdateRequest):UserUpdateResponse
}