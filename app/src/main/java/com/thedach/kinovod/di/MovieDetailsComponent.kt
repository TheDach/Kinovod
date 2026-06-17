package com.thedach.kinovod.di

import com.thedach.kinovod.presentation.movie.MovieDetailsFragment
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent (
    modules = [MovieDetailsModule::class]
)
interface MovieDetailsComponent {

    fun inject(movieDetailsFragment: MovieDetailsFragment)

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance @MovieIdQualifier id: Int
        ) : MovieDetailsComponent
    }
}