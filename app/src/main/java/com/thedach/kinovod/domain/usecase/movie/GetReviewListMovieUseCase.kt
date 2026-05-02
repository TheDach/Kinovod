package com.thedach.kinovod.domain.usecase.movie

import com.thedach.kinovod.domain.repository.MovieRepository

class GetReviewListMovieUseCase(
    private val repository: MovieRepository,
    private val movieId: Int
) {

    suspend operator fun invoke() = repository.getReviewListMovie(movieId)
}