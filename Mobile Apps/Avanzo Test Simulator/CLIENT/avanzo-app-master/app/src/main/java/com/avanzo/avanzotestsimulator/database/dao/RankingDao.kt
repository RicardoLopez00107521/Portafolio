package com.avanzo.avanzotestsimulator.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.avanzo.avanzotestsimulator.database.model.RankingModel


@Dao
interface RankingDao {

    //Muestra el ranking ordenado por puntos
    @Query("SELECT * FROM ranking_table ORDER BY points DESC")
    suspend fun getAllRanking(): List<RankingModel>

    //Busca un ranking por id
    @Query("SELECT * FROM ranking_table WHERE rankingId = :rankingId")
    suspend fun getRankingById(rankingId: Int): RankingModel?

    //Elimina todos los registros de la tabla ranking_table
    @Query("DELETE FROM ranking_table")
    suspend fun deleteAllRanking()

    @Insert
    suspend fun insertRanking(ranking: List<RankingModel>)

}