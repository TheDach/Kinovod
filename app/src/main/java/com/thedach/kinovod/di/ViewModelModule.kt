package com.thedach.kinovod.di

import androidx.lifecycle.ViewModel
import com.thedach.kinovod.presentation.movie.MovieDetailsViewModel
import com.thedach.kinovod.presentation.movie.MovieViewModel
import com.thedach.kinovod.presentation.profile.ProfileViewModel
import com.thedach.kinovod.presentation.room.VotingRoomViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MovieViewModel::class)
    fun bindMovieViewModel(viewModel: MovieViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    fun bindProfileViewModel(viewModel: ProfileViewModel): ViewModel
}