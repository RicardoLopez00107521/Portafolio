package com.avanzo.avanzotestsimulator.network.dto.notes

data class NotesRequest(
    val subjectId: String,
    val userId: String,
    val month: Int,
    val year: Int,
    val score: Double
)
