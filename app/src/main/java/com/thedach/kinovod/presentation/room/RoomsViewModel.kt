package com.thedach.kinovod.presentation.room

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thedach.kinovod.data.repository.RoomRepositoryImpl
import com.thedach.kinovod.data.repository.UserRepository
import com.thedach.kinovod.domain.model.room.Room
import com.thedach.kinovod.domain.usecase.room.AddMembersToRoomUseCase
import com.thedach.kinovod.domain.usecase.room.GetUserRoomsUseCase
import kotlinx.coroutines.launch

class RoomsViewModel : ViewModel() {

    private val roomsRepository = RoomRepositoryImpl()

    private val getUserRoomsUseCase = GetUserRoomsUseCase(roomsRepository)
    private val addMembersToRoomUseCase = AddMembersToRoomUseCase(roomsRepository)

    private val _roomsList = MutableLiveData<List<Room>>()
    val roomsList: LiveData<List<Room>> = _roomsList

    private val _isSuccessAddMember = MutableLiveData<Pair<Int, Boolean>>()
    val isSuccessAddMember: LiveData<Pair<Int, Boolean>> = _isSuccessAddMember

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isRefreshing = MutableLiveData<Boolean>(false)
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error


    private fun loadRooms() {
        if (_isLoading.value == true) return

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {

                _roomsList.value = getUserRoomsUseCase.invoke(UserRepository.getUserId())
                Log.d("RoomsViewModel: ", ((roomsList.value?.get(0) ?: "Ничего нет").toString()))


            } catch (ex: Exception) {
                ex.printStackTrace()
                _error.value = ex.message
            } finally {
                _isRefreshing.value = false
                _isLoading.value = false
            }
        }
    }

    fun connectToRoom(roomId: Int) {
        viewModelScope.launch {
            _error.value = null
            _isSuccessAddMember.value = Pair(-1, false)

            try {
                addMembersToRoomUseCase(
                    UserRepository.getUserId(),
                    roomId,
                    listOf(UserRepository.getUserId())
                )
                _isSuccessAddMember.value = Pair(roomId, true)

            } catch (ex: Exception) {
                ex.printStackTrace()
                _error.value = ex.message
            }
        }
    }

    fun refreshRooms(){
        loadRooms()
    }

    init {
        loadRooms()
    }
}