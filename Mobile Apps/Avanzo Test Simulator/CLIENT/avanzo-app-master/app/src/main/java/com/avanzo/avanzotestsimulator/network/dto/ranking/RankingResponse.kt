package com.avanzo.avanzotestsimulator.network.dto.ranking

import com.google.gson.annotations.SerializedName


data class RankingResponse(
    @SerializedName("data") val ranking: List<RankingRequest>
)
