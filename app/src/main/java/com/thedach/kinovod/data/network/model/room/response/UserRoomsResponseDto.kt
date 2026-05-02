package com.thedach.kinovod.data.network.model.room.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserRoomsResponseDto(

    @SerialName("message")
    val message: String?,

    @SerialName("rooms")
    val rooms: List<RoomResponseDto>?
)
