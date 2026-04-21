package com.thedach.kinovod.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val token: String = "",
    val userId: Int,
    val username: String,
    val email: String,
    val avatar: String?,

    val watchedList: List<Int>,
    val wishList: List<Int>,

    val friends: List<Friend>
) : Parcelable
