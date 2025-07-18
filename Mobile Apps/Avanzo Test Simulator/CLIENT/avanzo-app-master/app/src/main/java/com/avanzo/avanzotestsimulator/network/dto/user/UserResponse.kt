package com.avanzo.avanzotestsimulator.network.dto.user

import com.google.gson.annotations.SerializedName

class UserResponse (
    @SerializedName("data") val data: UserRequest
)