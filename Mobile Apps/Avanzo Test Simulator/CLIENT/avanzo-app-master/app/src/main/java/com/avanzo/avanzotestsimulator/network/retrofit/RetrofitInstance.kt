package com.avanzo.avanzotestsimulator.network.retrofit

import com.avanzo.avanzotestsimulator.network.service.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://calm-courage-production.up.railway.app/api/"

object RetrofitInstance{

    // a variable to store the token
    private var token = ""

    // a function to set the token

    fun setToken(token: String) {
        this.token = token
    }

    fun getToken(): String {
        println("TOKEN FROM RETROFIT: ${token}")

        return token
    }

    // a instance of Retrofit

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(
            OkHttpClient()
                .newBuilder()
                .addInterceptor { chain ->
                    chain.proceed(
                        chain.request()
                            .newBuilder()
                            .addHeader("Authorization", "Bearer $token")
                            .build()
                    )
                }
                .addInterceptor(HttpLoggingInterceptor().apply {
                    setLevel(HttpLoggingInterceptor.Level.HEADERS)
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                })
            .build()
        )
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // a function to get the login service from the Retrofit instance

    fun getLoginService(): AuthService {
        return retrofit.create(AuthService::class.java)
    }
    fun getRankingService(): RankingService {
        return retrofit.create(RankingService::class.java)
    }
    fun getSubjectService(): SubjectsService {
        return retrofit.create(SubjectsService::class.java)
    }
    fun getQuestionService(): QuestionsService {
        return retrofit.create(QuestionsService::class.java)
    }
    fun postQuiz(): QuizService {
        return retrofit.create(QuizService::class.java)
    }

    fun getUserService(): UserService {
        return retrofit.create(UserService::class.java)
    }

    fun getNotesService(): NotesService {
        return retrofit.create(NotesService::class.java)
    }
}