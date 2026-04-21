package com.thedach.kinovod.data.network.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FriendDto(

    @SerialName("userId")
    val userId: Int,

    @SerialName("username")
    val username: String,

    @SerialName("avatar")
    val avatar: String? = null,
)
