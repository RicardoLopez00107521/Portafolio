package com.avanzo.avanzotestsimulator.network.dto.quiz

data class QuizRequest(
    val mode: String,
    val subjectId: Int,
    val questionsIds: MutableList<Int>,
    val selectedAnswers: MutableList<String>
)