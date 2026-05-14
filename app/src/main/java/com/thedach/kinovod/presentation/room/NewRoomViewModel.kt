package com.thedach.kinovod.presentation.room

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thedach.kinovod.data.repository.RoomRepositoryImpl
import com.thedach.kinovod.data.repository.UserRepository
import com.thedach.kinovod.domain.model.profile.Friend
import com.thedach.kinovod.domain.model.room.CreateRoom
import com.thedach.kinovod.domain.model.room.Room
import com.thedach.kinovod.domain.model.room.VotingType
import com.thedach.kinovod.domain.usecase.room.CreateRoomUseCase
import kotlinx.coroutines.launch

class NewRoomViewModel : ViewModel() {

    private val roomsRepository = RoomRepositoryImpl()

    private val createRoomUseCase = CreateRoomUseCase(roomsRepository)

    private val _room = MutableLiveData<Room>()
    val room: LiveData<Room> = _room

    private val _isSuccessCreateRoom = MutableLiveData<Pair<Int, Boolean>>()
    val isSuccessCreateRoom: LiveData<Pair<Int, Boolean>> = _isSuccessCreateRoom

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isRefreshing = MutableLiveData<Boolean>(false)
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    fun createRoom(
        roomName: String,
        members: List<Friend>,
        selectedVotingDeadline: String?,
        votingType: VotingType,
        moviesLimit: Int,
        movieTypes: List<Int>,
        genres: List<String>,
        anonymousVoting: Boolean,
        compareWishlists: Boolean
    ) {
        if (_isLoading.value == true) return

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {

                _room.value = createRoomUseCase.invoke(
                    UserRepository.getUserId(),
                    CreateRoom(
                        name = roomName,
                        initialMembers = members.map { it.userId },
                        expiresAt = selectedVotingDeadline,
                        votingType = votingType,
                        votingAnonymous = anonymousVoting,
                        useWishlists = compareWishlists,
                        genres = genres,
                        movieType = movieTypes,
                        maxSuggestionsPerUser = moviesLimit
                    )
                )

            } catch (ex: Exception) {
                ex.printStackTrace()
                _error.value = ex.message
            } finally {
                _isRefreshing.value = false
                _isLoading.value = false
            }
        }
    }
}