package com.thedach.kinovod.data.network.model.room

data class MemberResponseDto(
    val userId: Int,
    val username: String,
    val userTag: String,
    val avatar: String?,
    val role: Int // 0 - creator (admin), 1 - member
)
