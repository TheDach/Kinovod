package com.thedach.kinovod.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.thedach.kinovod.domain.model.room.Room

object RoomsItemDiffCallback : DiffUtil.ItemCallback<Room>() {
    override fun areItemsTheSame(
        oldItem: Room,
        newItem: Room
    ): Boolean {
        return oldItem.roomId == newItem.roomId
    }

    override fun areContentsTheSame(
        oldItem: Room,
        newItem: Room
    ): Boolean {
        return oldItem == newItem
    }
}