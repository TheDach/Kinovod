package com.thedach.kinovod.di

import androidx.lifecycle.ViewModel
import com.thedach.kinovod.presentation.room.VotingRoomViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface VotingRoomModule {

    @Binds
    @IntoMap
    @ViewModelKey(VotingRoomViewModel::class)
    fun bindVotingRoomViewModel(viewModel: VotingRoomViewModel): ViewModel
}