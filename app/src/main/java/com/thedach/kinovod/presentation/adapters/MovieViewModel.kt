package com.thedach.kinovod.presentation.adapters

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

    private suspend fun loadMovies() {
        _movieList.value = getMovieListUseCase()
    }

    init {
        viewModelScope.launch {
            loadMovies()
        }
    }
}