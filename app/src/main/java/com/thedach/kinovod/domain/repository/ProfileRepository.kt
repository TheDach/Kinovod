package com.thedach.kinovod.domain.repository

import com.thedach.kinovod.domain.model.profile.User

interface ProfileRepository {

    suspend fun addFriend(userId: Int, friendTag: String)
    suspend fun refreshUserData(userId: Int)
}