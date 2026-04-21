package com.thedach.kinovod.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponseDto(

    @SerialName("message")
    val statusMessage: String,

    @SerialName("user")
    val user: UserDto
)
