package com.thedach.kinovod.presentation.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thedach.kinovod.data.repository.AuthenticationRepositoryImpl
import com.thedach.kinovod.domain.RegistrationUseCase
import kotlinx.coroutines.launch

class RegistrationViewModel : ViewModel() {

    private val authRepository = AuthenticationRepositoryImpl()

    private val register = RegistrationUseCase(authRepository)


    private val _isRegister = MutableLiveData<Boolean>()
    val isRegister: LiveData<Boolean>
        get() = _isRegister

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error


    fun registerNewUser(
        username: String,
        userTag: String,
        email: String,
        password: String
    ) {
        if (_isLoading.value == true) return

        viewModelScope.launch {

            _error.value = null
            _isLoading.value = true

            try {
                register.invoke(
                    username,
                    userTag,
                    email,
                    password
                )
                _isRegister.value = true

            } catch (ex: Exception) {
                _error.value = ex.message
                ex.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}