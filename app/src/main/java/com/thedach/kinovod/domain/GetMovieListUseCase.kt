package com.thedach.kinovod.domain

class GetMovieListUseCase(
    private val repository: MovieRepository
) {

    suspend operator fun invoke() = repository.getMovieList()
}