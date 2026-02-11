package com.example.prm.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TokenData(
    @SerializedName("token")
    val token: String,
    @SerializedName("expires_in")
    val expiresIn: Long? = null
)
