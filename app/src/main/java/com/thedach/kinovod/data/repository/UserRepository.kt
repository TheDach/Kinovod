package com.thedach.kinovod.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.thedach.kinovod.data.network.model.SyncUserDataRequest
import com.thedach.kinovod.domain.model.User
import com.thedach.network.ApiFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

object UserRepository {

    private val apiService = ApiFactory.apiService


    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User>
        get() = _currentUser


    private var syncJob: Job? = null
    private val syncDelay = 5000L
    private var hasPendingChanges = AtomicBoolean(false)


    fun setUser(user: User) {
        _currentUser.value = user
    }

    fun getIdListWatchedMovies(): List<Int> {
        return currentUser.value?.watchedList ?: emptyList()
    }

    fun getIdListWishMovies(): List<Int> {
        return currentUser.value?.wishList ?: emptyList()
    }

    fun addIdToListWishMovie(movieId: Int) {
        val currentUser = _currentUser.value ?: return

        val currentWishList = currentUser.wishList?.toMutableList() ?: mutableListOf()

        if (!currentWishList.contains(movieId)) {
            currentWishList.add(movieId)

            val updatedUser = currentUser.copy(
                wishList = currentWishList
            )
            _currentUser.value = updatedUser
            scheduleSync()
        }
    }

    fun addIdToListWatchedMovie(movieId: Int) {
        val currentUser = _currentUser.value ?: return

        val currentWatchedList = currentUser.watchedList?.toMutableList() ?: mutableListOf()

        if (!currentWatchedList.contains(movieId)) {
            currentWatchedList.add(movieId)

            val updatedUser = currentUser.copy(
                watchedList = currentWatchedList
            )
            _currentUser.value = updatedUser
            scheduleSync()
        }

        fun removeIdFromWishMovie(movieId: Int) {
            val currentUser = _currentUser.value ?: return

            val currentWishList = currentUser.wishList?.toMutableList() ?: mutableListOf()
            currentWishList.remove(movieId)

            val updatedUser = currentUser.copy(
                wishList = currentWishList
            )
            _currentUser.value = updatedUser
        }

        fun removeIdFromWatchedMovie(movieId: Int) {
            val currentUser = _currentUser.value ?: return

            val currentWatchedList = currentUser.watchedList?.toMutableList() ?: mutableListOf()
            currentWatchedList.remove(movieId)

            val updatedUser = currentUser.copy(
                watchedList = currentWatchedList
            )
            _currentUser.value = updatedUser
        }
    }

    fun removeIdFromWishMovie(movieId: Int): Boolean {
        val currentUser = _currentUser.value ?: return false

        val currentWishList = currentUser.wishList?.toMutableList() ?: mutableListOf()

        if (!currentWishList.contains(movieId)) {
            return false
        }

        currentWishList.remove(movieId)

        val updatedUser = currentUser.copy(
            wishList = currentWishList
        )
        _currentUser.value = updatedUser
        scheduleSync()
        return true
    }

    fun removeIdFromWatchedMovie(movieId: Int): Boolean {
        val currentUser = _currentUser.value ?: return false

        val currentWatchedList = currentUser.watchedList?.toMutableList() ?: mutableListOf()

        if (!currentWatchedList.contains(movieId)) {
            return false
        }

        currentWatchedList.remove(movieId)

        val updatedUser = currentUser.copy(
            watchedList = currentWatchedList
        )
        _currentUser.value = updatedUser
        scheduleSync()
        return true
    }

    fun isMovieInWishList(movieId: Int): Boolean {
        return _currentUser.value?.wishList?.contains(movieId) == true
    }

    fun isMovieInWatchedList(movieId: Int): Boolean {
        return _currentUser.value?.watchedList?.contains(movieId) == true
    }


    fun toggleWishList(movieId: Int): Boolean {
        return if (isMovieInWishList(movieId)) {
            removeIdFromWishMovie(movieId)
            false
        } else {
            addIdToListWishMovie(movieId)
            true
        }
    }

    fun toggleWatchedList(movieId: Int): Boolean {
        return if (isMovieInWatchedList(movieId)) {
            removeIdFromWatchedMovie(movieId)
            false
        } else {
            addIdToListWatchedMovie(movieId)
            true
        }
    }

    private fun scheduleSync() {
        hasPendingChanges.set(true)

        syncJob?.cancel()
        syncJob = CoroutineScope(Dispatchers.IO).launch {
            delay(syncDelay)
            syncWithServer()
        }
    }

    private suspend fun syncWithServer(): Boolean {
        val user = _currentUser.value ?: return false
        if (!hasPendingChanges.get()) return true

        try {
            val request = SyncUserDataRequest(
                userId = user.userId,
                watchedList = user.watchedList ?: emptyList(),
                wishList = user.wishList ?: emptyList(),
                friends = user.friends?.map { it.userId }
            )

            val response = apiService.syncUserData(request)

            if (response.isSuccessful) {
                Log.d("UserRepository", "Успешная синхронизация")
                return true
            } else {
                Log.e("UserRepository", "Ошибка синхронизации: ${response.code()}")
                return false
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Непредвиденная ошибка синхронизации: ${e.message}")
            return false
        }

    }

    private fun scheduleRetry() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(5000) // 5 секунд
            syncWithServer()
        }
    }

    suspend fun forceSync(): Boolean {
        cancelPendingSync()
        return syncWithServer().also {
            hasPendingChanges.set(false)
        }
    }

    private fun cancelPendingSync() {
        syncJob?.cancel()
        hasPendingChanges.set(false)
    }
}