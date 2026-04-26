package com.thedach.kinovod.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thedach.kinovod.data.repository.AuthenticationRepositoryImpl
import com.thedach.kinovod.data.repository.MovieRepositoryImpl
import com.thedach.kinovod.data.repository.UserRepository
import com.thedach.kinovod.domain.DeleteAccountUseCase
import com.thedach.kinovod.domain.GetMovieListByIdUseCase
import com.thedach.kinovod.domain.LogoutUseCase
import com.thedach.kinovod.domain.model.Movie
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val movieRepository = MovieRepositoryImpl
    private val getMovieListByIdUseCase = GetMovieListByIdUseCase(movieRepository)

    private val authRepository = AuthenticationRepositoryImpl()
    private val logoutUseCase = LogoutUseCase(authRepository)
    private val deleteAccountUseCase = DeleteAccountUseCase(authRepository)

    private val userRepository = UserRepository

    private val _movieWishList = MutableLiveData<List<Movie>>()
    val movieWishList: LiveData<List<Movie>>
        get() = _movieWishList

    private val _movieWatchedList = MutableLiveData<List<Movie>>()
    val movieWatchedList: LiveData<List<Movie>>
        get() = _movieWatchedList

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLoggingOut = MutableLiveData<Boolean>(false)
    val isLoggingOut: LiveData<Boolean> = _isLoggingOut

    private val _isDeletingAccount = MutableLiveData<Boolean>(false)
    val isDeletingAccount: LiveData<Boolean> = _isDeletingAccount

    private val _navigateToAuth = MutableLiveData<Boolean>(false)
    val navigateToAuth: LiveData<Boolean> = _navigateToAuth

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    private fun loadMovies() {

        if (_isLoading.value == true) return

        viewModelScope.launch {

            _error.value = null
            _isLoading.value = true

            try {
                _movieWatchedList.value = getMovieListByIdUseCase(userRepository.getIdListWatchedMovies())
                _movieWishList.value = getMovieListByIdUseCase(userRepository.getIdListWishMovies())

            } catch (ex: Exception) {
                _error.value = ex.message
                ex.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refreshMovies() {
        loadMovies()
    }

    fun deleteAccount() {
        if (_isDeletingAccount.value == true) return

        viewModelScope.launch {
            _error.value = null
            _isDeletingAccount.value = true
            _navigateToAuth.value = false

            try {
                deleteAccountUseCase(userRepository.getUserId())

                userRepository.clearUser()
                _navigateToAuth.value = true
            } catch (ex: Exception) {
                _error.value = ex.message
                ex.printStackTrace()
            } finally {
                _isDeletingAccount.value = false
            }
        }
    }

    fun logoutAccount() {
        if (_isLoggingOut.value == true) return

        viewModelScope.launch {
            _error.value = null
            _isLoggingOut.value = true
            _navigateToAuth.value = false

            try {
                logoutUseCase(userRepository.getUserId())

                userRepository.clearUser()
                _navigateToAuth.value = true
            } catch (ex: Exception) {
                _error.value = ex.message
                ex.printStackTrace()
            } finally {
                _isLoggingOut.value = false
            }
        }
    }

    fun onNavigationComplete() {
        _navigateToAuth.value = false
    }


    init {
        loadMovies()
    }
}