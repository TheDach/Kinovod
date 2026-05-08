package com.thedach.kinovod.domain.model.room

data class RoomMember(
    val userId: Int,
    val username: String,
    val userTag: String,
    val avatar: String?,
    val role: MemberRole
)
