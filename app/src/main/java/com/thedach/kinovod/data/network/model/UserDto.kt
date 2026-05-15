package com.thedach.kinovod.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val token: String = "",
    @SerialName("userId")
    val userId: Int,

    @SerialName("username")
    val username: String,

    @SerialName("userTag")
    val userTag: String,

    @SerialName("email")
    val email: String,

    @SerialName("avatar")
    val avatar: String? = null,

    @SerialName("watchedList")
    val watchedList: List<Int>? = emptyList(),

    @SerialName("wishList")
    val wishList: List<Int>? = emptyList(),

    @SerialName("friends")
    val friends: List<FriendDto>? = emptyList()
)