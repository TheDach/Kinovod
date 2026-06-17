package com.thedach.kinovod.presentation.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thedach.kinovod.domain.usecase.movie.GetMovieListUseCase
import com.thedach.kinovod.domain.model.movie.Movie
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieViewModel @Inject constructor(
    private val getMovieListUseCase: GetMovieListUseCase
)  : ViewModel() {

    private val _movieList = MutableLiveData<List<Movie>>()
    val movieList: LiveData<List<Movie>>
        get() = _movieList

    private val _filteredMovieList = MutableLiveData<List<Movie>>()
    val filteredMovieList: LiveData<List<Movie>> = _filteredMovieList

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isRefreshing = MutableLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    private val _isSendingSuggestion = MutableLiveData<Boolean>()
    val isSendingSuggestion: LiveData<Boolean> = _isSendingSuggestion

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    private var searchJob: Job? = null
    private var loadJob: Job? = null

    private var currentQuery = ""
    private val selectedGenres = mutableListOf<String>()
    private val activeChips = mutableListOf<String>()

    private fun loadMovies(force: Boolean = false) {
        if (_isLoading.value == true && !force) return

        loadJob?.cancel()

        loadJob = viewModelScope.launch {

            _isLoading.value = true
            _error.value = null

            try {
                _movieList.value = getMovieListUseCase(genreName = selectedGenres.toList())
                applyFilters()

            } catch (ex: Exception) {
                _error.value = ex.message
                ex.printStackTrace()

            } finally {
                _isRefreshing.value = false
                _isLoading.value = false
            }
        }
    }

    fun loadMoviesByGenres(genres: List<String>) {
        selectedGenres.clear()
        selectedGenres.addAll(genres)
        loadMovies(force = true)
    }

    fun refreshMovies() {
        _isRefreshing.value = true
        loadMovies()
    }

    fun searchMovies(query: String) {
        currentQuery = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300)
            applyFilters()
        }
    }

    private fun applyFilters() {
        val allMovies = _movieList.value ?: emptyList()

        val filtered = if (currentQuery.isBlank()) {
            allMovies
        } else {
            allMovies.filter { movie ->
                movie.name.contains(currentQuery, ignoreCase = true) ||
                        movie.genres.any { genre ->
                            genre.contains(currentQuery, ignoreCase = true)
                        }
            }
        }

        _filteredMovieList.value = filtered
    }

    fun getActiveChips(): List<String> = activeChips.toList()

    fun addActiveChip(genre: String) {
        if (!activeChips.contains(genre)) {
            activeChips.add(genre)
        }
    }

    fun removeActiveChip(genre: String) {
        activeChips.remove(genre)
    }

    fun clearSearch() {
        currentQuery = ""
        applyFilters()
    }

    fun clearGenres() {
        selectedGenres.clear()
        activeChips.clear()
        loadMovies()
    }

    init {
        loadMovies()
    }
}