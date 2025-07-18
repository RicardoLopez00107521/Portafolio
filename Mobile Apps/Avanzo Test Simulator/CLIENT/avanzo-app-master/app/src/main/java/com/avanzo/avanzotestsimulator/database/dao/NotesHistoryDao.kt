package com.avanzo.avanzotestsimulator.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.avanzo.avanzotestsimulator.database.model.NotesHistoryModel
@Dao
interface NotesHistoryDao {

    //Muestra el historial de notas
    @Query("SELECT * FROM notes_history_table ORDER BY total_score DESC")
    suspend fun getAllNotesHistory(): List<NotesHistoryModel>

    //Inserta el historial de notas
    @Insert
    suspend fun insertNotesHistory(notesHistory: List<NotesHistoryModel>)

    //Elimina todos los registros de la tabla notes_history_table
    @Query("DELETE FROM notes_history_table")
    suspend fun deleteAllNotesHistory()

    @Query("SELECT SUM(total_score) FROM notes_history_table WHERE month = :month")
    suspend fun getNotesByMonth(month: String): String

    @Query("SELECT SUM(total_score) FROM notes_history_table WHERE year = :year")
    suspend fun getNotesByYear(year: String): String
}