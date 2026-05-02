package com.thedach.kinovod.data.network.model.room.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MemberResponseDto(
    @SerialName("userId")
    val userId: Int,

    @SerialName("username")
    val username: String,

    @SerialName("userTag")
    val userTag: String,

    @SerialName("avatar")
    val avatar: String?,

    @SerialName("role")
    val role: Int // 0 - creator (admin), 1 - member
)
