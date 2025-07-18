package com.avanzo.avanzotestsimulator.repositories

import android.util.Log
import com.avanzo.avanzotestsimulator.database.dao.SubjectDao
import com.avanzo.avanzotestsimulator.database.model.SubjectModel
import com.avanzo.avanzotestsimulator.network.service.SubjectsService
import kotlinx.coroutines.Job
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SubjectRepository(private val subjectDao: SubjectDao, private val api: SubjectsService) {

        // Variable para controlar el Job
        private var job: Job? = null

        suspend fun getSubjects(): List<SubjectModel> {
                // Si ya hay un Job en curso, cancelarlo
                job?.cancel()

                // Crear un nuevo Job
                job = Job()

                return withContext(Dispatchers.IO + job!!) {
                        try {
                                val subject = api.getSubjects()
                                val idSubjectApi = subject.subjects.map { s -> s.subjectId }
                                val idSubjectDb = subjectDao.getAllSubjects().map { s -> s.subjectCode.toInt() }

                                if (idSubjectApi.sorted() != idSubjectDb.sorted()) {
                                        subjectDao.deleteAllSubjects()
                                        val subjectMap = subject.subjects.map { s ->
                                                SubjectModel(s.subjectId, s.subjectName, "drawable")
                                        }
                                        subjectDao.insertSubject(subjectMap)
                                        return@withContext subjectDao.getAllSubjects()
                                }

                                return@withContext subjectDao.getAllSubjects()
                        } catch (e: Exception) {
                                Log.d("ComunicationError", "Error: ${e.message}")
                                // En caso de error, devolvemos los datos almacenados en la base de datos local
                                return@withContext subjectDao.getAllSubjects()
                        }
                }
        }
}
