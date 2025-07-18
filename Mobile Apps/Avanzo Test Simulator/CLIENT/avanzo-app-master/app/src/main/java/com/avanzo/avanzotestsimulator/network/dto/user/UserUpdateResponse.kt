package com.avanzo.avanzotestsimulator.network.dto.user

import com.google.gson.annotations.SerializedName

class UserUpdateResponse (
    @SerializedName("message") val updated: String
    )