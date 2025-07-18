package com.avanzo.avanzotestsimulator.repositories

import android.util.Log
import com.avanzo.avanzotestsimulator.database.dao.UserDao
import com.avanzo.avanzotestsimulator.database.model.UserModel
import com.avanzo.avanzotestsimulator.network.ApiResponse
import com.avanzo.avanzotestsimulator.network.dto.user.UserUpdateRequest
import com.avanzo.avanzotestsimulator.network.service.UserService
import kotlinx.coroutines.Job
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class UserRepository(private val userDao: UserDao, private val api: UserService) {

        // Variable para manejar el Job
        private var job: Job? = null

        // Función para actualizar un usuario
        suspend fun updateUser(password: String, password_confirmation: String): ApiResponse<String> {
                // Si ya hay un Job en curso, cancelarlo
                job?.cancel()

                // Crear un nuevo Job para la solicitud actual
                job = Job()

                return withContext(Dispatchers.IO + job!!) {
                        try {
                                val response = api.updateUser(UserUpdateRequest(password, password_confirmation))
                                return@withContext ApiResponse.Success(response.updated) // TODO: Verificar que tipo de respuesta manda la API
                        } catch (e: HttpException) {
                                if (e.code() == 400) {
                                        return@withContext ApiResponse.ErrorWithMessage("==Error==")
                                }
                                return@withContext ApiResponse.Error(e)
                        } catch (e: IOException) {
                                return@withContext ApiResponse.Error(e)
                        }
                }
        }

        // Función para obtener el usuario desde la API o de la base de datos local
        suspend fun getUsers(): UserModel {
                // Si ya hay un Job en curso, cancelarlo
                job?.cancel()

                // Crear un nuevo Job para la solicitud actual
                job = Job()

                return withContext(Dispatchers.IO + job!!) {
                        try {
                                userDao.deleteAllUsers()
                                val user = api.getUser()
                                val userMap = UserModel(user.data.firstName, user.data.lastName, user.data.email, user.data.points.toString())
                                userDao.insertUser(userMap)
                                return@withContext userMap
                        } catch (e: Exception) {
                                Log.d("ComunicationError", "Error: ${e.message}")
                                return@withContext userDao.getUser()
                        }
                }
        }

        // Función para obtener el usuario local (sin hacer solicitud a la API)
        suspend fun getLocalUser(): UserModel {
                return userDao.getUser()
        }
}
