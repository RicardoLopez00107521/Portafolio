package com.avanzo.avanzotestsimulator.repositories

import android.util.Log
import com.avanzo.avanzotestsimulator.database.dao.NotesHistoryDao
import com.avanzo.avanzotestsimulator.database.model.NotesHistoryModel
import com.avanzo.avanzotestsimulator.network.service.NotesService
import kotlinx.coroutines.Job
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NotesHistoryRepository(private val notesHistoryDao: NotesHistoryDao, private val api: NotesService) {

    // Variable para controlar el Job
    private var job: Job? = null

    suspend fun getNotesHistory(): List<NotesHistoryModel> {
        // Si ya hay un Job en curso, cancelarlo
        job?.cancel()

        // Crear un nuevo Job
        job = Job()

        return withContext(Dispatchers.IO + job!!) {
            try {
                // Hacemos la solicitud a la API de notas
                val notesHistory = api.getNotes()

                // Obtenemos los ID de notas de la API y de la base de datos
                val idNotesHistoryApi = notesHistory.notes.map { r -> r.subjectId }
                val idNotesHistoryDb = notesHistoryDao.getAllNotesHistory().map { r -> r.noteId }

                // Si los datos son diferentes, borramos los datos locales y los insertamos nuevamente
                if (idNotesHistoryApi.sorted() != idNotesHistoryDb.sorted()) {
                    notesHistoryDao.deleteAllNotesHistory()
                    val notesHistoryMap = notesHistory.notes.map { r ->
                        NotesHistoryModel(r.month, r.year, r.score)
                    }
                    notesHistoryDao.insertNotesHistory(notesHistoryMap)
                }

                // Devolvemos todas las notas almacenadas en la base de datos
                return@withContext notesHistoryDao.getAllNotesHistory()

            } catch (e: Exception) {
                Log.d("ComunicationError", "Error: ${e.message}")
                // En caso de error, devolvemos las notas almacenadas localmente
                return@withContext notesHistoryDao.getAllNotesHistory()
            }
        }
    }

    // Métodos adicionales para acceder a notas locales
    suspend fun getNotesLocal() = notesHistoryDao.getAllNotesHistory()

    suspend fun getNotesByMonth(month: String) = notesHistoryDao.getNotesByMonth(month)

    suspend fun getNotesByYear(year: String) = notesHistoryDao.getNotesByYear(year)
}
