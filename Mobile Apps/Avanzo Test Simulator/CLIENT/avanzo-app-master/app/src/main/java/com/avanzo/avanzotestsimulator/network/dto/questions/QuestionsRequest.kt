package com.avanzo.avanzotestsimulator.network.dto.questions

data class QuestionsRequest(
    val questionId: Int,
    val question: String,
    val subjectId: Int,
    val image: String,
    val answers: List<AnswerRequest>,
    val correctAnswer: Int,
)
