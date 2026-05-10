package com.thedach.kinovod.presentation.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thedach.kinovod.domain.model.room.Room

class VotingRoomViewModelFactory(
    private val roomId: Int
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VotingRoomViewModel::class.java)) {
            return VotingRoomViewModel(roomId) as T
        }
        throw RuntimeException("Unknown view model class $modelClass")
    }
}