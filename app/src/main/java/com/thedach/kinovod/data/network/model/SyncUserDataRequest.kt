package com.thedach.kinovod.data.network.model

data class SyncUserDataRequest(
    val userId: Int,
    val username: String? = null,
    val avatar: String? = null,

    val watchedList: List<Int>? = null,
    val wishList: List<Int>? = null,
    val friends: List<Int>? = null
)
