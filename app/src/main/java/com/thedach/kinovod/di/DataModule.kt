package com.thedach.kinovod.di

import com.thedach.kinovod.data.repository.MovieRepositoryImpl
import com.thedach.kinovod.domain.repository.MovieRepository
import com.thedach.network.ApiFactory
import com.thedach.network.ApiService
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindMovieRepository(impl: MovieRepositoryImpl): MovieRepository

    companion object {

        @Provides
        fun provideApiService(): ApiService {
            return ApiFactory.apiService
        }
    }
}