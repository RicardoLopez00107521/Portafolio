package com.avanzo.avanzotestsimulator.repositories

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.avanzo.avanzotestsimulator.network.ApiResponse
import com.avanzo.avanzotestsimulator.network.dto.quiz.QuizRequest
import com.avanzo.avanzotestsimulator.network.dto.quiz.QuizResponse
import com.avanzo.avanzotestsimulator.network.service.QuizService
import kotlinx.coroutines.Job
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class QuizRepository(
    private val api: QuizService
) {

    // Variable para manejar el Job
    private var job: Job? = null

    // Cambiar el tipo de retorno a ApiResponse<Float> para devolver el puntaje
    suspend fun postAnswers(mode: String, subjectId: Int, questionsIds: MutableList<Int>, selectedAnswers: MutableList<String>)
            : ApiResponse<Float> {

        // Si ya hay un Job en curso, cancelarlo
        job?.cancel()

        // Crear un nuevo Job para la solicitud actual
        job = Job()

        return withContext(Dispatchers.IO + job!!) {
            try {
                // Realizamos la solicitud a la API para enviar las respuestas
                val response: QuizResponse = api.postQuestionsWithAnswer(QuizRequest(mode, subjectId, questionsIds, selectedAnswers))

                // Obtenemos el puntaje de la respuesta
                val score = response.score
                // Devolvemos el puntaje dentro de una respuesta exitosa
                ApiResponse.Success(score)
            } catch (e: HttpException) {
                if (e.code() == 400) {
                    // En caso de un error de HTTP 400, devolvemos un mensaje de error personalizado
                    ApiResponse.ErrorWithMessage("==Error==")
                } else {
                    // En caso de otro error HTTP, devolvemos el error general
                    ApiResponse.Error(e)
                }
            } catch (e: IOException) {
                Log.d("ComunicationError", "Error: ${e.message}")
                // En caso de error de red, devolvemos el error de entrada/salida
                ApiResponse.Error(e)
            }
        }
    }
}
