package com.avanzo.avanzotestsimulator.network.service

import com.avanzo.avanzotestsimulator.network.dto.subjects.SubjectsRequest
import com.avanzo.avanzotestsimulator.network.dto.subjects.SubjectsResponse
import retrofit2.http.GET

interface SubjectsService {

    @GET("subjects")
    suspend fun getSubjects(): SubjectsResponse
}