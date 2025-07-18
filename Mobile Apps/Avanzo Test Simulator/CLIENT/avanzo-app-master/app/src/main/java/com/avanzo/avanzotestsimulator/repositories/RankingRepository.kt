package com.avanzo.avanzotestsimulator.repositories

import android.util.Log
import com.avanzo.avanzotestsimulator.database.dao.RankingDao
import com.avanzo.avanzotestsimulator.database.model.RankingModel
import com.avanzo.avanzotestsimulator.network.service.RankingService
import kotlinx.coroutines.Job
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RankingRepository(private val rankingDao: RankingDao, private val api: RankingService) {

    // Variable para controlar el Job
    private var job: Job? = null

    suspend fun getRankings(): List<RankingModel> {
        // Si ya hay un Job en curso, cancelarlo
        job?.cancel()

        // Crear un nuevo Job
        job = Job()

        return withContext(Dispatchers.IO + job!!) {
            try {
                // Borramos rankings anteriores en la base de datos
                rankingDao.deleteAllRanking()

                // Realizamos la solicitud a la API
                val getRanking = api.getRanking()
                val idRankingApi = getRanking.ranking.map { r -> r.id }
                val idRankingDb = rankingDao.getAllRanking().map { r -> r.rankingId }

                // Comprobamos si los rankings en la API son diferentes a los de la base de datos
                if (idRankingApi.sorted() != idRankingDb.sorted()) {
                    rankingDao.deleteAllRanking()
                    val rankingMap = getRanking.ranking.map { r -> RankingModel(r.name, r.points) }
                    rankingDao.insertRanking(rankingMap)
                }

                return@withContext rankingDao.getAllRanking()

            } catch (e: Exception) {
                Log.d("ComunicationError", "Error: ${e.message}")
                // En caso de error, devolvemos los rankings almacenados en la base de datos local
                return@withContext rankingDao.getAllRanking()
            }
        }
    }

    suspend fun getRankingById(id: Int) = rankingDao.getRankingById(id)
}
