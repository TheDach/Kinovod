package com.thedach.kinovod.domain.usecase.movie

import com.thedach.kinovod.domain.repository.MovieRepository
import javax.inject.Inject

class GetMovieListByIdUseCase @Inject constructor(
    private val repository: MovieRepository
) {

    suspend operator fun invoke(movieId: List<Int>) = repository.getMovieListById(movieId)
}