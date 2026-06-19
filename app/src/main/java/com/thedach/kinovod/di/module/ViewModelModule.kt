package com.thedach.kinovod.di.module

import androidx.lifecycle.ViewModel
import com.thedach.kinovod.presentation.auth.LoginViewModel
import com.thedach.kinovod.presentation.auth.RegistrationViewModel
import com.thedach.kinovod.presentation.movie.MovieViewModel
import com.thedach.kinovod.presentation.profile.FriendListViewModel
import com.thedach.kinovod.presentation.profile.ProfileViewModel
import com.thedach.kinovod.presentation.room.NewRoomViewModel
import com.thedach.kinovod.presentation.room.RoomsViewModel
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

    @Binds
    @IntoMap
    @ViewModelKey(FriendListViewModel::class)
    fun bindFriendListViewModel(viewModel: FriendListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RoomsViewModel::class)
    fun bindRoomsViewModel(viewModel: RoomsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewRoomViewModel::class)
    fun bindNewRoomViewModel(viewModel: NewRoomViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegistrationViewModel::class)
    fun bindRegistrationViewModel(viewModel: RegistrationViewModel): ViewModel
}