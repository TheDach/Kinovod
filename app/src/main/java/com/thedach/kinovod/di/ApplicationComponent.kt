package com.thedach.kinovod.di

import android.app.Activity
import com.thedach.kinovod.presentation.movie.MainActivity
import com.thedach.kinovod.presentation.movie.MovieFragment
import com.thedach.kinovod.presentation.profile.ProfileFragment
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class, ViewModelModule::class])
interface ApplicationComponent {

    fun inject(mainActivity: MainActivity)
    fun inject(movieFragment: MovieFragment)
    fun inject(profileFragment: ProfileFragment)

    fun movieDetailsComponent(): MovieDetailsComponent.Factory
    fun votingRoomComponent(): VotingRoomComponent.Factory
}