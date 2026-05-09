package com.thedach.kinovod.domain.model.room

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RoomMember(
    val userId: Int,
    val username: String,
    val userTag: String,
    val avatar: String?,
    val role: MemberRole
) : Parcelable
