package com.thedach.kinovod.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thedach.kinovod.data.repository.MovieRepositoryImpl
import com.thedach.kinovod.domain.GetMovieListUseCase
import com.thedach.kinovod.domain.model.Movie
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {

    private val movieRepository = MovieRepositoryImpl()

    private val getMovieListUseCase = GetMovieListUseCase(movieRepository)

    private val _movieList = MutableLiveData<List<Movie>>()
    val movieList: LiveData<List<Movie>>
        get() = _movieList

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isRefreshing = MutableLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    private fun loadMovies() {
        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null

            try{
                _movieList.value = getMovieListUseCase()

            } catch (ex: Exception) {
                _error.value = ex.message

            } finally {
                _isRefreshing.value = false
                _isLoading.value = false
            }
        }
    }

    fun refreshMovies() {
        _isRefreshing.value = true
        loadMovies()
    }

    init {
        loadMovies()
    }
}