package com.avanzo.avanzotestsimulator.database.model

data class QuestionsModel(
    val id: Int, //num de pregunta
    val question: String, //Pregunta
    val image: Int,
    val optionOne: String,
    val optionTwo: String,
    val optionThree: String,
    val optionFour: String,
    val correctAnswer: Int
)



