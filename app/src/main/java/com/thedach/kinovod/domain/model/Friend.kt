package com.thedach.kinovod.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Friend(
    val userId: Int,
    val username: String,
    val avatar: String?
) : Parcelable
