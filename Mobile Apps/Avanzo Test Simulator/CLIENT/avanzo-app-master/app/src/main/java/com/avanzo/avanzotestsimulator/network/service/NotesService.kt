package com.avanzo.avanzotestsimulator.network.service

import com.avanzo.avanzotestsimulator.network.dto.notes.NoteResponse
import retrofit2.http.GET

interface NotesService {

    @GET("quiz/myNotes")
    suspend fun getNotes(): NoteResponse
}