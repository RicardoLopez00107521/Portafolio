package com.avanzo.avanzotestsimulator.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_table")
data class QuizModel(
    @PrimaryKey(autoGenerate = true) var quizId: Long = 0L,
    @ColumnInfo(name = "userId") val userId: String,
    @ColumnInfo(name = "subjectId") val subjectId: String,
    @ColumnInfo(name = "status") val status: String, //Ni idea
    @ColumnInfo(name = "score") val score: String,
    @ColumnInfo(name = "mode") val mode: String,
){
    constructor(userId: String, subjectId: String, status: String, score: String, mode: String):
            this(0, userId, subjectId, status, score, mode)
}

