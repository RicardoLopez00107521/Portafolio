package com.avanzo.avanzotestsimulator.database.model

import androidx.annotation.Size
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "question_table")
data class QuestionModel(
    @PrimaryKey(autoGenerate = true) var questionId: Long = 0L,
    @ColumnInfo(name = "numP") val numP: Int,
    @ColumnInfo(name = "question")
    val question: String, //question
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "optionOne")
    val optionOne: String,
    @ColumnInfo(name = "optionTwo")
    val optionTwo: String,
    @ColumnInfo(name = "optionThree")
    val optionThree: String,
    @ColumnInfo(name = "optionFour")
    val optionFour: String,
    @ColumnInfo(name = "correctAnswer") val correctAnswer: Int,
    @ColumnInfo(name = "subjectId") val subjectId: Int
)
{
    constructor(numP: Int, question: String, image: String, optionOne: String, optionTwo: String, optionThree: String, optionFour: String, correctAnswer: Int, subjectId: Int):
            this(0, numP, question, image, optionOne, optionTwo, optionThree, optionFour, correctAnswer, subjectId)
}
