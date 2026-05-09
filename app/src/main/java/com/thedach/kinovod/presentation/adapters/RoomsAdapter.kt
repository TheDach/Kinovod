package com.thedach.kinovod.presentation.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import com.squareup.picasso.Picasso
import com.thedach.kinovod.R
import com.thedach.kinovod.databinding.ItemRoomBinding
import com.thedach.kinovod.domain.model.room.Room
import com.thedach.kinovod.domain.model.room.RoomMember
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class RoomsAdapter : ListAdapter<Room, RoomsViewHolder>(RoomsItemDiffCallback) {

    var onRoomClickListener: ((Room) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RoomsViewHolder {
        val binding = ItemRoomBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RoomsViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: RoomsViewHolder,
        position: Int
    ) {
        val room = getItem(position)
        val context = holder.binding.root.context

        with(holder.binding) {
            tvRoomName.text = room.name

            tvHostBadgeRoom.visibility = if (room.isAdmin) View.VISIBLE else View.GONE

            if (!room.genres.isNullOrEmpty()) {
                chipRoomGenres.visibility = View.VISIBLE
                chipRoomGenres.text = room.genres.first()
            } else {
                chipRoomGenres.visibility = View.GONE
            }

            if (!room.movieType.isNullOrEmpty()) {
                chipMovieType.visibility = View.VISIBLE
                chipMovieType.text = getMovieTypeString(context, room.movieType.first())
            } else {
                chipMovieType.visibility = View.GONE
            }

            displayMemberAvatars(holder.binding, room.members)

            tvRoomItemInfo.text = getRoomInfoString(context, room)

            // Время истечения
            if (!room.expiresAt.isNullOrEmpty()) {
                chipExpiresAtMovie.visibility = View.VISIBLE
                chipExpiresAtMovie.text = formatExpiresAt(room.expiresAt)
            } else {
                chipExpiresAtMovie.visibility = View.GONE
            }

            root.setOnClickListener {
                onRoomClickListener?.invoke(room)
            }
        }
    }

    private fun formatExpiresAt(expiresAt: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")

            val outputFormat = SimpleDateFormat("dd.MM", Locale.getDefault())

            val date = inputFormat.parse(expiresAt)
            if (date != null) {
                "до ${outputFormat.format(date)}"
            } else {
                expiresAt
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            expiresAt
        }
    }

    private fun getMovieTypeString(context: Context, movieType: Int): String {
        return when (movieType) {
            0 -> context.getString(R.string.movie_type_any)
            1 -> context.getString(R.string.movie_type_movie)
            2 -> context.getString(R.string.movie_type_series)
            else -> context.getString(R.string.movie_type_any)
        }
    }

    private fun displayMemberAvatars(
        binding: ItemRoomBinding,
        members: List<RoomMember>
    ) {
        val lastMembers = members.takeLast(3)

        binding.ivAvatarFirstRoom.visibility = View.GONE
        binding.ivAvatarSecondRoom.visibility = View.GONE
        binding.ivAvatarThirdRoom.visibility = View.GONE

        when {
            lastMembers.size >= 3 -> {
                loadAvatar(binding.ivAvatarThirdRoom, lastMembers[2].avatar)
                loadAvatar(binding.ivAvatarSecondRoom, lastMembers[1].avatar)
                loadAvatar(binding.ivAvatarFirstRoom, lastMembers[0].avatar)
                binding.ivAvatarFirstRoom.visibility = View.VISIBLE
                binding.ivAvatarSecondRoom.visibility = View.VISIBLE
                binding.ivAvatarThirdRoom.visibility = View.VISIBLE
            }
            lastMembers.size == 2 -> {
                loadAvatar(binding.ivAvatarSecondRoom, lastMembers[1].avatar)
                loadAvatar(binding.ivAvatarFirstRoom, lastMembers[0].avatar)
                binding.ivAvatarFirstRoom.visibility = View.VISIBLE
                binding.ivAvatarSecondRoom.visibility = View.VISIBLE
            }
            lastMembers.size == 1 -> {
                loadAvatar(binding.ivAvatarFirstRoom, lastMembers[0].avatar)
                binding.ivAvatarFirstRoom.visibility = View.VISIBLE
            }
        }
    }

    private fun loadAvatar(imageView: ImageView, avatarUrl: String?) {
        if (!avatarUrl.isNullOrEmpty()) {
            Picasso.get()
                .load(avatarUrl)
                .into(imageView)
        } else {
            imageView.setImageResource(R.drawable.default_avatar_image)
        }
    }

    private fun getRoomInfoString(
        context: android.content.Context,
        room: Room
    ): String {
        val membersCount = room.members.size
        val suggestionsCount = room.suggestions.size
        val maxSuggestions = room.maxSuggestionsPerUser ?: 0

        return context.getString(
            R.string.room_info_format,
            membersCount,
            suggestionsCount,
            maxSuggestions
        )
    }
}