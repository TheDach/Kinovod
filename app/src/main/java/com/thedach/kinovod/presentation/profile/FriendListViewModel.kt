package com.thedach.kinovod.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thedach.kinovod.data.repository.UserRepository
import com.thedach.kinovod.domain.usecase.profile.AddFriendUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.invoke

class FriendListViewModel @Inject constructor(
    private val addFriendUseCase: AddFriendUseCase
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    fun addFriend(friendTag: String) {
        if (_isLoading.value == true) return

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null

            try {
                addFriendUseCase.invoke(UserRepository.getUserId(), friendTag)

            } catch (ex: Exception) {
                _error.value = ex.message
                ex.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}