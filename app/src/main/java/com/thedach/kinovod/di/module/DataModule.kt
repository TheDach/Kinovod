package com.thedach.kinovod.di.module

import com.thedach.kinovod.data.repository.AuthenticationRepositoryImpl
import com.thedach.kinovod.data.repository.MovieRepositoryImpl
import com.thedach.kinovod.data.repository.ProfileRepositoryImpl
import com.thedach.kinovod.data.repository.RoomRepositoryImpl
import com.thedach.kinovod.di.ApplicationScope
import com.thedach.kinovod.domain.repository.AuthenticationRepository
import com.thedach.kinovod.domain.repository.MovieRepository
import com.thedach.kinovod.domain.repository.ProfileRepository
import com.thedach.kinovod.domain.repository.RoomRepository
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

    @ApplicationScope
    @Binds
    fun bindRoomRepository(impl: RoomRepositoryImpl): RoomRepository

    @ApplicationScope
    @Binds
    fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository

    @ApplicationScope
    @Binds
    fun bindAuthenticationRepository(impl: AuthenticationRepositoryImpl): AuthenticationRepository

    companion object {

        @Provides
        fun provideApiService(): ApiService {
            return ApiFactory.apiService
        }
    }
}