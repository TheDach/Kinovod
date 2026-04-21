package com.thedach.kinovod.domain

class GetMovieListByIdUseCase(
    private val repository: MovieRepository,
    private val movieId: Int
) {

    suspend operator fun invoke() = repository.getMovieListById(movieId)
}