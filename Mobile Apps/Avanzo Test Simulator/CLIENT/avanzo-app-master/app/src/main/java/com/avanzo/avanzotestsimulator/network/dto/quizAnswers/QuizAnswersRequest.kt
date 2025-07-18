package com.avanzo.avanzotestsimulator.network.dto.quizAnswers

data class QuizAnswersRequest(
    val questionId: Int,
    val selectedAnswer: String,
)
