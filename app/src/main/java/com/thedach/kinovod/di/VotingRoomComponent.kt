package com.thedach.kinovod.di

import com.thedach.kinovod.presentation.room.VotingRoomFragment
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent (
    modules = [VotingRoomModule::class]
)
interface VotingRoomComponent {

    fun inject(votingRoomFragment: VotingRoomFragment)

    @Subcomponent.Factory
    interface Factory{

        fun create(
            @BindsInstance @RoomIdQualifier roomId: Int
        ): VotingRoomComponent
    }
}