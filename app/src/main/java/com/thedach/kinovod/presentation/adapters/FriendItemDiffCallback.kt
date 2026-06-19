package com.thedach.kinovod.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.thedach.kinovod.domain.model.profile.Friend

object FriendItemDiffCallback : DiffUtil.ItemCallback<Friend>() {
    override fun areItemsTheSame(
        oldItem: Friend,
        newItem: Friend
    ): Boolean {
        return oldItem.userId == newItem.userId
    }

    override fun areContentsTheSame(
        oldItem: Friend,
        newItem: Friend
    ): Boolean {
        return oldItem == newItem
    }
}