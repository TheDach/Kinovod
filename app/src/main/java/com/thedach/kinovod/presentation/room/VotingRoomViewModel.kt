package com.thedach.kinovod.presentation.room

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thedach.kinovod.data.repository.MovieRepositoryImpl
import com.thedach.kinovod.data.repository.RoomRepositoryImpl
import com.thedach.kinovod.data.repository.UserRepository
import com.thedach.kinovod.domain.model.movie.Movie
import com.thedach.kinovod.domain.model.room.Room
import com.thedach.kinovod.domain.model.room.SuggestionWithMovie
import com.thedach.kinovod.domain.usecase.movie.GetMovieListByIdUseCase
import com.thedach.kinovod.domain.usecase.room.AddUserVotesUseCase
import com.thedach.kinovod.domain.usecase.room.GetRoomDetailsUseCase
import com.thedach.kinovod.domain.usecase.room.RemoveUserFromRoomUseCase
import kotlinx.coroutines.launch

class VotingRoomViewModel(
    private val roomId: Int
) : ViewModel() {

    private val roomRepository = RoomRepositoryImpl()
    private val movieRepository = MovieRepositoryImpl

    private val getRoomDetailsUseCase = GetRoomDetailsUseCase(roomRepository)
    private val addUserVotesUseCase = AddUserVotesUseCase(roomRepository)
    private val removeUserFromRoomUseCase = RemoveUserFromRoomUseCase(roomRepository)
    private val getMovieListByIdUseCase = GetMovieListByIdUseCase(movieRepository)

    private val _room = MutableLiveData<Room>()
    val room: LiveData<Room>
        get() = _room

    private val _suggestionWithMovie = MutableLiveData<List<SuggestionWithMovie>>()
    val suggestionWithMovie: LiveData<List<SuggestionWithMovie>> = _suggestionWithMovie

    private val _isSuccessLeaving = MutableLiveData<Boolean>()
    val isSuccessLeaving: LiveData<Boolean> = _isSuccessLeaving

    private val _movies = MutableLiveData<List<Movie>>()

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isRefreshing = MutableLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error


    private fun loadRoom() {
        if (_isLoading.value == true) return

        viewModelScope.launch {

            _isLoading.value = true
            _isRefreshing.value = true
            _error.value = null

            try {
                _room.value = getRoomDetailsUseCase(
                    roomId = roomId,
                    userId = UserRepository.getUserId()
                )

                val movieIdsList = _room.value?.suggestions?.map { it.movieId } ?: emptyList()
                if (movieIdsList.isNotEmpty()) {
                    _movies.value = getMovieListByIdUseCase(movieIdsList)
                }

                _suggestionWithMovie.value = createSuggestionWithMovie(
                    _room.value ?: throw Exception("Error: room is empty"),
                    _movies.value ?: throw Exception("Error: movies is empty")
                )


            } catch (ex: Exception) {
                _error.value = ex.message
                ex.printStackTrace()

            } finally {
                _isRefreshing.value = false
                _isLoading.value = false
            }
        }
    }

    fun refreshRoomData() {
        loadRoom()
    }

    private fun createSuggestionWithMovie(
        room: Room,
        movies: List<Movie>
    ): List<SuggestionWithMovie> {

        val movieMap = movies.associateBy { it.id }

        return room.suggestions.mapNotNull { suggestion ->
            // Находим Movie по movieId из suggestion
            val movie = movieMap[suggestion.movieId]

            movie?.let {
                SuggestionWithMovie(
                    suggestion = suggestion,
                    movie = movie,
                    progressPercent = calculateProgressPercent(
                        room.members.size,
                        suggestion.voters.size
                    ),
                    votingAnonymous = room.votingAnonymous ?: false,
                    votingType = room.votingType
                )
            }
        }
    }


    fun leaveRoom() {
        viewModelScope.launch {

            _error.value = null
            _isSuccessLeaving.value = false

            try {
                removeUserFromRoomUseCase(
                    UserRepository.getUserId(),
                    _room.value?.roomId ?: throw Exception("Room is empty"),
                    UserRepository.getUserId()
                )

                _isSuccessLeaving.value = true
                refreshRoomData()

            } catch (ex: Exception) {
                _error.value = ex.message
                ex.printStackTrace()
            }
        }
    }

    fun submitVote(movieIds: List<Int>) {
        if (_isLoading.value == true) return

        viewModelScope.launch {
            _isLoading.value = true
            _isRefreshing.value = true
            _error.value = null

            try {
                Log.d("submitVote: ", movieIds.toString())
                addUserVotesUseCase(
                    UserRepository.getUserId(),
                    _room.value?.roomId ?: throw Exception("Room is empty"),
                    movieIds
                )

            } catch (ex: Exception) {
                _isLoading.value = false
                _isRefreshing.value = false
                _error.value = ex.message
                ex.printStackTrace()
            } finally {
                refreshRoomData()
            }
        }
    }

    private fun calculateProgressPercent(amountMembers: Int, votersCount: Int): Int {
        if (amountMembers <= 0) return 0
        return ((votersCount.toDouble() / amountMembers) * PERCENT_100).toInt()
    }

    init {
        loadRoom()
    }


    companion object {
        private const val PERCENT_100 = 100
    }
}