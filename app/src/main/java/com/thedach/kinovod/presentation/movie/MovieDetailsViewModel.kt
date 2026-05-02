package com.thedach.kinovod.presentation.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thedach.kinovod.data.repository.MovieRepositoryImpl
import com.thedach.kinovod.data.repository.UserRepository
import com.thedach.kinovod.domain.usecase.movie.GetReviewListMovieUseCase
import com.thedach.kinovod.domain.model.movie.Review
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    movieId: Int
) : ViewModel() {

    private val movieRepository = MovieRepositoryImpl

    private val getReviewListMovieUseCase = GetReviewListMovieUseCase(movieRepository, movieId)

    private val _reviewList = MutableLiveData<List<Review>>()
    val reviewList: LiveData<List<Review>>
        get() = _reviewList

    private val _isInWishList = MutableLiveData<Boolean>()
    val isInWishList: LiveData<Boolean> = _isInWishList

    private val _isInWatchedList = MutableLiveData<Boolean>()
    val isInWatchedList: LiveData<Boolean> = _isInWatchedList

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error


    private fun loadReviews() {
        if (_isLoading.value == true) return

        viewModelScope.launch {

            _error.value = null
            _isLoading.value = true

            try {

                _reviewList.value = getReviewListMovieUseCase()

            } catch (ex: Exception) {
                _error.value = ex.message
                _isLoading.value = false
                ex.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveMovieToWishList(movieId: Int) {
        UserRepository.addIdToListWishMovie(movieId)
        _isInWishList.value = true
    }

    fun saveMovieToWatchedList(movieId: Int) {
        UserRepository.addIdToListWatchedMovie(movieId)
        _isInWatchedList.value = true
    }

    fun removeFromWishList(movieId: Int) {
        UserRepository.removeIdFromWishMovie(movieId)
        _isInWishList.value = false
    }

    fun removeFromWatchedList(movieId: Int) {
        UserRepository.removeIdFromWatchedMovie(movieId)
        _isInWatchedList.value = false
    }

    fun checkMovieStatus(movieId: Int) {
        _isInWishList.value = UserRepository.isMovieInWishList(movieId)
        _isInWatchedList.value = UserRepository.isMovieInWatchedList(movieId)
    }

    fun toggleWishList(movieId: Int) {
        viewModelScope.launch {
            val newState = UserRepository.toggleWishList(movieId)
            _isInWishList.value = newState
        }
    }

    fun toggleWatchedList(movieId: Int) {
        viewModelScope.launch {
            val newState = UserRepository.toggleWatchedList(movieId)
            _isInWatchedList.value = newState
        }
    }

    init {
        loadReviews()
        checkMovieStatus(movieId)
    }
}