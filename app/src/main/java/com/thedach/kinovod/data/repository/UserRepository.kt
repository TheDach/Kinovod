package com.thedach.kinovod.data.repository

import com.thedach.kinovod.domain.model.User

object UserRepository {
    var currentUser: User? = null
        private set

    fun setUser(user: User) {
        currentUser = user
    }

    fun clearUser() {
        currentUser = null
    }
}