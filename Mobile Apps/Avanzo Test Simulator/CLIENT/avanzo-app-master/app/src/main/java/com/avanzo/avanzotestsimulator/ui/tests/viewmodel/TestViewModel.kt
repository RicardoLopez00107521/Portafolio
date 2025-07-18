package com.avanzo.avanzotestsimulator.ui.tests.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.avanzo.avanzotestsimulator.AvanzoReviewerApplication
import com.avanzo.avanzotestsimulator.network.ApiResponse
import com.avanzo.avanzotestsimulator.repositories.QuestionRepository
import com.avanzo.avanzotestsimulator.repositories.QuizRepository
import com.avanzo.avanzotestsimulator.ui.login.LoginUiStatus
import com.avanzo.avanzotestsimulator.ui.tests.TestUiStatus
import kotlinx.coroutines.launch

class TestViewModel(private val repository: QuestionRepository, private val quizRepository: QuizRepository) : ViewModel() {
    var id = MutableLiveData("")
    var question = MutableLiveData("")
    var image = MutableLiveData("")
    var optionOne = MutableLiveData("")
    var optionTwo = MutableLiveData("")
    var optionThree = MutableLiveData("")
    var optionFour = MutableLiveData("")
    var correctAnswer = MutableLiveData("")

    suspend fun getQuestions(subjectName: String) = repository.getQuestions(subjectName)

    suspend fun getQuestion() = repository.getQuestion()

    private val _status = MutableLiveData<TestUiStatus>(TestUiStatus.Resume)
    val status: MutableLiveData<TestUiStatus>
        get() = _status

    suspend fun postAnswers(
        mode: String,
        subjectId: Int,
        questionsIds: MutableList<Int>,
        selectedAnswers: MutableList<String>
    ) = quizRepository.postAnswers(mode, subjectId, questionsIds, selectedAnswers)

    suspend fun getQuestionBySubject(subjectId: Int) = repository.getQuestionBySubject(subjectId)

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AvanzoReviewerApplication
                TestViewModel(app.questionRepository, app.quizRepository)
            }
        }
    }

    //lateinit var repo: QuestionRepository
}