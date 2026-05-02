package com.thedach.kinovod.data.network.model.room.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoomDetailsResponseDto(

    @SerialName("message")
    val message: String?,

    @SerialName("room")
    val room: RoomResponseDto?
)
