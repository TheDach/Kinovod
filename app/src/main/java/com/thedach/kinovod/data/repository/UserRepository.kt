package com.thedach.kinovod.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.thedach.kinovod.domain.model.User

object UserRepository {

    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User>
        get() = _currentUser


    fun setUser(user: User) {
        _currentUser.value = user
    }
}