package com.avanzo.avanzotestsimulator.network.service

import com.avanzo.avanzotestsimulator.network.dto.ranking.RankingResponse
import retrofit2.http.GET

interface RankingService {

    @GET("rankings/globalRank")
    suspend fun getRanking(): RankingResponse
}

