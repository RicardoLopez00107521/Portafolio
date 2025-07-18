package com.avanzo.avanzotestsimulator.repositories

import com.avanzo.avanzotestsimulator.network.ApiResponse
import com.avanzo.avanzotestsimulator.network.dto.login.LoginRequest
import com.avanzo.avanzotestsimulator.network.dto.register.RegisterRequest
import com.avanzo.avanzotestsimulator.network.service.AuthService
import retrofit2.HttpException
import java.io.IOException

class CredentialsRepository(private val api: AuthService) {

    suspend fun login(email: String, password: String): ApiResponse<String> {
        try {

            val response = api.login(LoginRequest(email, password))
            return ApiResponse.Success(response.token)
        } catch (e: HttpException) {
            if (e.code() == 400) {
                return ApiResponse.ErrorWithMessage("Invalid email or password")
            }
            return ApiResponse.Error(e)
        } catch (e: IOException) {
            return ApiResponse.Error(e)
        }
    }

    suspend fun register(
        firstname: String,
        lastname: String,
        role: String,
        email: String,
        password: String,
        password_confirmation: String
    ): ApiResponse<String> {
        try {
            val response = api.register(RegisterRequest(firstname, lastname, role, email, password, password_confirmation))
            return ApiResponse.Success(response.message)
        } catch (e: HttpException) {
            if (e.code() == 400) {
                return ApiResponse.ErrorWithMessage("Invalid name, email or password")
            }
            return ApiResponse.Error(e)
        } catch (e: IOException) {
            return ApiResponse.Error(e)
        }
    }
}