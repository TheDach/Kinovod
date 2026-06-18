package com.thedach.kinovod.di

import com.thedach.kinovod.di.module.DataModule
import com.thedach.kinovod.di.module.ViewModelModule
import com.thedach.kinovod.presentation.movie.MainActivity
import com.thedach.kinovod.presentation.movie.MovieFragment
import com.thedach.kinovod.presentation.profile.ProfileFragment
import com.thedach.kinovod.presentation.room.NewRoomFragment
import com.thedach.kinovod.presentation.room.RoomsFragment
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class, ViewModelModule::class])
interface ApplicationComponent {

    fun inject(mainActivity: MainActivity)
    fun inject(movieFragment: MovieFragment)
    fun inject(profileFragment: ProfileFragment)
    fun inject(roomsFragment: RoomsFragment)
    fun inject(newRoomFragment: NewRoomFragment)

    fun movieDetailsComponent(): MovieDetailsComponent.Factory
    fun votingRoomComponent(): VotingRoomComponent.Factory
}