package com.avanzo.avanzotestsimulator.network.dto.subjects

data class SubjectsRequest(
    val subjectId: String,
    val subjectName: String,
    val subjectCode: Long,
    val subjectDescription: String,
)
