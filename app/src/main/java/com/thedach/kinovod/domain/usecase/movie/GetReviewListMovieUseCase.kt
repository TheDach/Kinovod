package com.thedach.kinovod.domain.usecase.movie

import com.thedach.kinovod.di.qualifiers.MovieIdQualifier
import com.thedach.kinovod.domain.repository.MovieRepository
import javax.inject.Inject

class GetReviewListMovieUseCase @Inject constructor(
    private val repository: MovieRepository,
    @MovieIdQualifier private val movieId: Int
) {

    suspend operator fun invoke() = repository.getReviewListMovie(movieId)
}