package com.avanzo.avanzotestsimulator.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_history_table")
data class NotesHistoryModel(
    @PrimaryKey(autoGenerate = true) var noteId: Long = 0L,
    @ColumnInfo(name = "month") val month: Int,
    @ColumnInfo(name = "year") val year: Int,
    @ColumnInfo(name = "total_score") val score: Double
)
{
    constructor(month: Int, year: Int, score: Double):
            this(0, month, year, score)
}
