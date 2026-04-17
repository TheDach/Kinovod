package com.thedach.kinovod.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thedach.kinovod.data.repository.MovieRepositoryImpl
import com.thedach.kinovod.domain.GetReviewListMovieUseCase
import com.thedach.kinovod.domain.model.Review
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    movieId: Int
): ViewModel() {

    private val movieRepository = MovieRepositoryImpl

    private val getReviewListMovieUseCase = GetReviewListMovieUseCase(movieRepository, movieId)

    private val _reviewList = MutableLiveData<List<Review>>()
    val reviewList: LiveData<List<Review>>
        get() = _reviewList

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error


    private fun loadReviews() {
        viewModelScope.launch {

            _error.value = null

            try {

                _reviewList.value = getReviewListMovieUseCase()

            } catch (ex: Exception) {
                _error.value = ex.message
                ex.printStackTrace()
            }
        }
    }

    init {
        loadReviews()
    }
}