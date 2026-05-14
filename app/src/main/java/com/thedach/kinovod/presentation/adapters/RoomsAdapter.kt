package com.thedach.kinovod.presentation.adapters


import android.content.Context
import android.util.Log
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
        if (expiresAt == null || expiresAt == "null" || expiresAt.isEmpty()) {
            return "без срока"
        }
        // Нормализуем строку
        var normalized = expiresAt.trim()
        normalized = normalized.replace('T', ' ')
        return try {


            // Убираем миллисекунды
            if (normalized.contains(".")) {
                normalized = normalized.substringBefore(".")
            }

            // Парсим дату
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val localDateTime = LocalDateTime.parse(normalized, formatter)

            // Форматируем вывод
            "до ${localDateTime.format(DateTimeFormatter.ofPattern("dd.MM"))}"
        } catch (ex: Exception) {
            try {
                // Пробуем другой формат (без секунд)
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                val localDateTime = LocalDateTime.parse(normalized, formatter)
                "до ${localDateTime.format(DateTimeFormatter.ofPattern("dd.MM"))}"
            } catch (ex2: Exception) {
                Log.e("RoomsAdapter", "Error parsing date: $expiresAt", ex2)
                expiresAt
            }
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
        context: Context,
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