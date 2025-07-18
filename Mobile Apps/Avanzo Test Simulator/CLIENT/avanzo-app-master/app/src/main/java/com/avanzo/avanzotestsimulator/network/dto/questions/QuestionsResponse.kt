package com.avanzo.avanzotestsimulator.network.dto.questions

import com.google.gson.annotations.SerializedName

data class QuestionsResponse (
    @SerializedName("data") val questions: List<QuestionsRequest>
)