package com.avanzo.avanzotestsimulator.ui.results.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.avanzo.avanzotestsimulator.AvanzoReviewerApplication
import com.avanzo.avanzotestsimulator.database.model.SubjectModel
import com.avanzo.avanzotestsimulator.repositories.NotesHistoryRepository
import com.avanzo.avanzotestsimulator.repositories.SubjectRepository

class ResultViewModel(private val repository: SubjectRepository, private val notesHistoryRepository: NotesHistoryRepository): ViewModel()  {
    // Aqui se implementa LiveData
    var name = MutableLiveData("")
    var image = MutableLiveData("")

    suspend fun getSubjects() = repository.getSubjects()

    suspend fun getNotesLocal() = notesHistoryRepository.getNotesLocal()

    suspend fun getNotesHistory() = notesHistoryRepository.getNotesHistory()

    suspend fun getNotesbyMonth(month: String) = notesHistoryRepository.getNotesByMonth(month)

    suspend fun getNotesbyYear(year: String) = notesHistoryRepository.getNotesByYear(year)

    // Funcion para implementar el RecyclerView
    fun setSelectedSubject(subject: SubjectModel) {
        name.value = subject.name
        image.value = subject.image
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AvanzoReviewerApplication
                ResultViewModel(app.subjectRepository, app.notesHistoryRepository)
            }
        }
    }
}