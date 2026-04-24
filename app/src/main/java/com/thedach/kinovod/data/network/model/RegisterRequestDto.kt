package com.thedach.kinovod.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequestDto(
    @SerialName("username")
    val username: String,

    @SerialName("userTag")
    val userTag: String,

    @SerialName("email")
    val email: String,

    @SerialName("password")
    val password: String
)
