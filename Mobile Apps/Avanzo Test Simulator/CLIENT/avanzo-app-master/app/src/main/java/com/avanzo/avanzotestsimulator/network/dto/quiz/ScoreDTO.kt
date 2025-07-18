package com.avanzo.avanzotestsimulator.network.dto.quiz

import com.google.gson.annotations.SerializedName

data class ScoreDTO (
    @SerializedName("score") val score: Float,
    @SerializedName("correctAnswers") val correctAnswers: Int
)