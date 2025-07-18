package com.avanzo.avanzotestsimulator

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.avanzo.avanzotestsimulator.database.AvanzoTestSimulatorDataBase
import com.avanzo.avanzotestsimulator.network.retrofit.RetrofitInstance
import com.avanzo.avanzotestsimulator.network.retrofit.RetrofitInstance.setToken
import com.avanzo.avanzotestsimulator.repositories.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class AvanzoReviewerApplication: Application()  {
    override fun onCreate() {
        super.onCreate()
        GlobalScope.launch {
            val preferences = retrofitDataStore.data.firstOrNull()
            val token = preferences?.get(AUTH_TOKEN)
            if (token == null) {
                saveAuthToken("")
            } else {
                setToken(token)
            }
        }
    }

    //Instanciamos la base de datos
    private val database: AvanzoTestSimulatorDataBase by lazy {
        AvanzoTestSimulatorDataBase.newInstance(this)
    }

    // Inyectar los dao en los repositorios
    val rankingRepository: RankingRepository by lazy {
        RankingRepository(database.rankingDao(),RetrofitInstance.getRankingService())
    }

    val subjectRepository: SubjectRepository by lazy {
        SubjectRepository(database.subjectDao(),RetrofitInstance.getSubjectService())
    }

    val questionRepository: QuestionRepository by lazy {
        QuestionRepository(database.questionDao(),RetrofitInstance.getQuestionService())
    }

    val quizRepository: QuizRepository by lazy {
        QuizRepository(RetrofitInstance.postQuiz())
    }
    val userRepository: UserRepository by lazy {
        UserRepository(database.userDao(),RetrofitInstance.getUserService())
    }

    val notesHistoryRepository: NotesHistoryRepository by lazy {
        NotesHistoryRepository(database.notesHistoryDao(),RetrofitInstance.getNotesService())
    }

    private val Context.retrofitDataStore: DataStore<Preferences> by preferencesDataStore(name = "retrofit").also {
        RetrofitInstance.apply {
            GlobalScope.launch {
                retrofitDataStore.data.collect { preferences ->
                    setToken(preferences[AUTH_TOKEN] ?: "")
                }
            }
        }
    }
    val AUTH_TOKEN = stringPreferencesKey("user_token")

    // Get the API service from the Retrofit instance and set the token
    private fun getAPIService() = with(RetrofitInstance) {
        setToken(getToken())
        getLoginService()
    }

    fun getToken() = retrofitDataStore.data


    // Initialize the repository

    val credentialsRepository: CredentialsRepository by lazy {
        CredentialsRepository(getAPIService())
    }

    // a function to save the token in the SharedPreferences

    suspend fun saveAuthToken(token: String) {
        retrofitDataStore.edit { preferences ->
            preferences[AUTH_TOKEN] = token
        }
        RetrofitInstance.setToken(token)
    }

}