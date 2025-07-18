package com.avanzo.avanzotestsimulator.network.dto.notes

import com.google.gson.annotations.SerializedName

class NoteResponse (
    @SerializedName("data") val notes: List<NotesRequest>
)