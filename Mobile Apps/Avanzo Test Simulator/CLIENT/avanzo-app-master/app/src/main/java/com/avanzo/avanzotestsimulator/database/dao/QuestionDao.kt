package com.avanzo.avanzotestsimulator.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.avanzo.avanzotestsimulator.database.model.QuestionModel


@Dao
interface QuestionDao {

    //Muestra todas las preguntas
    @Query("SELECT * FROM question_table")
    suspend fun getAllQuestions(): List<QuestionModel>

    //Inserta una pregunta
    @Insert
    suspend fun insertQuestion(question: List<QuestionModel>)

    //Elimina todas las preguntas
    @Query("DELETE FROM question_table")
    suspend fun deleteAllQuestions()

    @Query("SELECT * FROM question_table WHERE subjectId = :subjectId LIMIT 15")
    suspend fun getQuestionsBySubject(subjectId: Int): List<QuestionModel>

    /*@Query("SELECT * FROM question_table WHERE subjectId = :subjectId")
    suspend fun getQuestionsBySubject(subjectId: Int): List<QuestionModel>*/
}