package com.avanzo.avanzotestsimulator.network.dto.subjects

import com.google.gson.annotations.SerializedName

data class SubjectsResponse(
    @SerializedName("data") val subjects: List<SubjectsRequest>
)
