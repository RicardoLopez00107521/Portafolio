package com.avanzo.avanzotestsimulator.ui.ranking.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.avanzo.avanzotestsimulator.AvanzoReviewerApplication
import com.avanzo.avanzotestsimulator.database.model.RankingModel
import com.avanzo.avanzotestsimulator.repositories.RankingRepository

class RankingViewModel(private val repository: RankingRepository): ViewModel() {

    // Aqui se implementa LiveData
    var name = MutableLiveData("")
    var points = MutableLiveData("")

    suspend fun getRankings() = repository.getRankings()

    //suspend fun getRankingRemote() = repository.getRankingRemote()

    // Funcion para implementar el RecyclerView
    fun setSelectedRanking(ranking: RankingModel) {
        name.value = ranking.name
        points.value = ranking.points.toString()
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AvanzoReviewerApplication
                RankingViewModel(app.rankingRepository)
            }
        }
    }
}