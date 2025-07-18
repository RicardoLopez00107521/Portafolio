package com.avanzo.avanzotestsimulator.network.service

import com.avanzo.avanzotestsimulator.network.dto.quiz.QuizRequest
import com.avanzo.avanzotestsimulator.network.dto.quiz.QuizResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface QuizService {
    @POST("quiz/getScore")
    suspend fun postQuestionsWithAnswer(@Body answerData: QuizRequest): QuizResponse
}