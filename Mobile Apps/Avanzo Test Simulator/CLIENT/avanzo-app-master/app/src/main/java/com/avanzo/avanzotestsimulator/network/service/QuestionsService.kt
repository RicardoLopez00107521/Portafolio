package com.avanzo.avanzotestsimulator.network.service

import com.avanzo.avanzotestsimulator.network.dto.questions.QuestionsRequest
import com.avanzo.avanzotestsimulator.network.dto.questions.QuestionsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface QuestionsService {
    @GET("question/getQuestions/{subject}")
    suspend fun getQuestions(@Path("subject") subject: String): QuestionsResponse
}