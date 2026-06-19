package com.thedach.kinovod.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.thedach.kinovod.R
import com.thedach.kinovod.databinding.ItemFriendBinding
import com.thedach.kinovod.domain.model.profile.Friend

class FriendAdapter(
    private val context: Context
) : ListAdapter<Friend, FriendViewHolder>(FriendItemDiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FriendViewHolder {
        val binding = ItemFriendBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FriendViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: FriendViewHolder,
        position: Int
    ) {
        val friend = getItem(position)

        with(holder.binding) {
            tvFriendName.text = friend.username
            tvUserTag.text = context.getString(R.string.tv_friend_tag)
                .format(friend.userTag)

            friendAvatar.setCardBackgroundColor(
                context.getColor(
                    R.color.purple_500
                )
            )
            tvFriendAvatar.text = friend.username.take(1).uppercase()
        }
    }
}