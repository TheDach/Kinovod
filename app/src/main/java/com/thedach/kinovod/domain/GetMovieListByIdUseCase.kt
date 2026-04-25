package com.thedach.kinovod.domain

class GetMovieListByIdUseCase(
    private val repository: MovieRepository
) {

    suspend operator fun invoke(movieId: List<Int>) = repository.getMovieListById(movieId)
}