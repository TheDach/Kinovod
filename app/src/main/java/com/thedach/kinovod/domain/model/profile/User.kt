package com.thedach.kinovod.domain.model.profile

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val token: String = "",
    val userId: Int,
    val username: String,
    val userTag: String,
    val email: String,
    val avatar: String? = null,

    val watchedList: List<Int>? = null,
    val wishList: List<Int>? = null,

    val friends: List<Friend>? = null
) : Parcelable
