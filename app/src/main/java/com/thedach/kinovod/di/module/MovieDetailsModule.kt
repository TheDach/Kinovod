package com.thedach.kinovod.di.module

import androidx.lifecycle.ViewModel
import com.thedach.kinovod.presentation.movie.MovieDetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface MovieDetailsModule {

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailsViewModel::class)
    fun bindMovieDetailsViewModel(viewModel: MovieDetailsViewModel): ViewModel
}