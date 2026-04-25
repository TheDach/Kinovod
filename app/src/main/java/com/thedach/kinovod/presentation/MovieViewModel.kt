package com.thedach.kinovod.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thedach.kinovod.data.repository.MovieRepositoryImpl
import com.thedach.kinovod.domain.GetMovieListUseCase
import com.thedach.kinovod.domain.model.Movie
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {

    private val movieRepository = MovieRepositoryImpl

    private val getMovieListUseCase = GetMovieListUseCase(movieRepository)

    private val _movieList = MutableLiveData<List<Movie>>()
    val movieList: LiveData<List<Movie>>
        get() = _movieList

    private val _filteredMovieList = MutableLiveData<List<Movie>>()
    val filteredMovieList: LiveData<List<Movie>> = _filteredMovieList

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isRefreshing = MutableLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    private var searchJob: Job? = null
    private var currentQuery = ""

    private fun loadMovies() {
        viewModelScope.launch {

            _isLoading.value = true
            _error.value = null

            try{
                _movieList.value = getMovieListUseCase()

            } catch (ex: Exception) {
                _error.value = ex.message
                ex.printStackTrace()

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

    fun searchMovies(query: String) {
        currentQuery = query

        searchJob?.cancel()

        // Запускаем новый поиск с задержкой (debounce)
        searchJob = viewModelScope.launch {
            delay(300)
            filterMovies(query)
        }
    }

    private fun filterMovies(query: String) {
        val allMovies = _movieList.value ?: emptyList()

        val filtered = if (query.isBlank()) {
            allMovies
        } else {
            allMovies.filter { movie ->
                movie.name.contains(query, ignoreCase = true) ||
                        movie.genres.any { genre ->
                            genre.contains(query, ignoreCase = true)
                        }
            }
        }

        _filteredMovieList.value = filtered
    }

    fun clearSearch() {
        currentQuery = ""
        filterMovies("")
    }

    init {
        loadMovies()
    }
}