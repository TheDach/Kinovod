package com.thedach.kinovod.domain.model.room

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RoomMatch(
    val voters: List<Int>,
    val count: Int
) : Parcelable
