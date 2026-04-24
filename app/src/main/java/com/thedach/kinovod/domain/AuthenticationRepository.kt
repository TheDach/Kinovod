package com.thedach.kinovod.domain

import com.thedach.kinovod.domain.model.User

interface AuthenticationRepository {

    suspend fun login(
        email: String,
        password: String
    ): User

    suspend fun registration(
        username: String,
        userTag: String,
        email: String,
        password: String
    ): User
}