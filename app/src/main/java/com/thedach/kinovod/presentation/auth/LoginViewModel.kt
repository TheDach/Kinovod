package com.thedach.kinovod.presentation.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thedach.kinovod.data.repository.AuthenticationRepositoryImpl
import com.thedach.kinovod.domain.LoginUseCase
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val authRepository = AuthenticationRepositoryImpl()

    private val login = LoginUseCase(authRepository)


    private val _isLogin = MutableLiveData<Boolean>()
    val isLogin: LiveData<Boolean>
        get() = _isLogin

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    fun loginUser(
        email: String,
        password: String
    ) {
        if (_isLoading.value == true) return

        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null

            try {
                login.invoke(
                    email,
                    password
                )
                _isLogin.value = true
                _isLoading.value = true

            } catch (ex: Exception) {
                _error.value = ex.message
                ex.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}