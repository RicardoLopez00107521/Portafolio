package com.avanzo.avanzotestsimulator.network.dto.quiz

import com.google.gson.annotations.SerializedName

data class QuizResponse (
    @SerializedName("message") val message: String,
    @SerializedName("data") val score: Float
)