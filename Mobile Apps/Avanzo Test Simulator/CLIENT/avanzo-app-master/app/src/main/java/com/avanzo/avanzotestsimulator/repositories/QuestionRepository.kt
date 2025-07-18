package com.avanzo.avanzotestsimulator.repositories

import android.util.Log
import com.avanzo.avanzotestsimulator.database.dao.QuestionDao
import com.avanzo.avanzotestsimulator.database.model.QuestionModel
import com.avanzo.avanzotestsimulator.network.service.QuestionsService
import kotlinx.coroutines.Job
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class QuestionRepository(private val questionDao: QuestionDao, private val api: QuestionsService) {

    // Variable para manejar el Job
    private var job: Job? = null

    suspend fun getQuestions(subjectName: String): List<QuestionModel> {

        // Si ya hay un Job en curso, cancelarlo
        job?.cancel()

        // Crear un nuevo Job para la solicitud actual
        job = Job()

        return withContext(Dispatchers.IO + job!!) {
            try {
                val questions = api.getQuestions(subjectName)
                val size = questions.questions.size
                Log.d("QuestionRepository", size.toString())

                val idQuestionsApi = questions.questions.map { q -> q.questionId }
                val idQuestionsDb = questionDao.getAllQuestions().map { q -> q.questionId }

                // Si los IDs de las preguntas en la API no coinciden con los de la base de datos, se actualizan
                if (idQuestionsApi.sorted() != idQuestionsDb.sorted()) {
                    questionDao.deleteAllQuestions()

                    val questionsMap = questions.questions.map { q ->
                        val questionText = q.question.ifEmpty { "Default question" }
                        val optionOne = q.answers.getOrNull(0)?.answer ?: "Default Answer 1"
                        val optionTwo = q.answers.getOrNull(1)?.answer ?: "Default Answer 2"
                        val optionThree = q.answers.getOrNull(2)?.answer ?: "Default Answer 3"
                        val optionFour = q.answers.getOrNull(3)?.answer ?: "Default Answer 4"

                        QuestionModel(q.questionId, questionText, "Image", optionOne, optionTwo, optionThree, optionFour, q.correctAnswer, q.subjectId)
                    }
                    questionDao.insertQuestion(questionsMap)
                }
                return@withContext questionDao.getAllQuestions()
            } catch (e: Exception) {
                Log.d("ComunicationError", "Error: ${e.message}")
                // Si ocurre un error, retornamos las preguntas locales
                return@withContext questionDao.getAllQuestions()
            }
        }
    }

    suspend fun getQuestion() = questionDao.getAllQuestions()

    suspend fun getQuestionBySubject(subjectId: Int): List<QuestionModel> {
        Log.d("Enviando", "Enviado $subjectId")
        getQuestion()
        return questionDao.getQuestionsBySubject(subjectId)
    }
}
