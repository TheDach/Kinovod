package com.thedach.kinovod.domain

class GetReviewListMovieUseCase(
    private val repository: MovieRepository,
    private val movieId: Int
) {

    suspend operator fun invoke() = repository.getReviewListMovie(movieId)
}