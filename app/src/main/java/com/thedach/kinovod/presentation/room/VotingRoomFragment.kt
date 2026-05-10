package com.thedach.kinovod.presentation.room

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.marginEnd
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.chip.Chip
import com.squareup.picasso.Picasso
import com.thedach.kinovod.R
import com.thedach.kinovod.databinding.FragmentVotingRoomBinding
import com.thedach.kinovod.domain.model.room.MemberRole
import com.thedach.kinovod.domain.model.room.Room
import com.thedach.kinovod.domain.model.room.RoomMember
import com.thedach.kinovod.presentation.adapters.VotingAdapter
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class VotingRoomFragment : Fragment() {

    private var _binding: FragmentVotingRoomBinding? = null
    private val binding: FragmentVotingRoomBinding
        get() = _binding ?: throw RuntimeException("FragmentVotingRoomBinding == null")

    private val roomArgs by navArgs<VotingRoomFragmentArgs>()

    private val viewModelFactory: VotingRoomViewModelFactory by lazy {
        VotingRoomViewModelFactory(
            roomArgs.room.roomId
        )
    }

    private val viewModel : VotingRoomViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[VotingRoomViewModel::class.java]
    }



    private lateinit var votingAdapter: VotingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVotingRoomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupRecyclerView()

        observeViewModel()
        setupClickListeners()
    }


    private fun observeViewModel() {
        viewModel.room.observe(viewLifecycleOwner) {room ->
            setupRoomData(room)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {loading ->
            if (loading) {
                binding.progressBarLoadingVotes.visibility = View.VISIBLE
            } else {
                binding.progressBarLoadingVotes.visibility = View.GONE
            }
        }

        viewModel.suggestionWithMovie.observe(viewLifecycleOwner) {suggestionWithMovies ->
            votingAdapter.submitList(suggestionWithMovies)
        }
    }

    private fun setupClickListeners() {
        binding.btnComeBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnSuggestMovie.setOnClickListener {
            // Заглушка
            Toast.makeText(requireContext(), "Предложить фильм (в разработке)", Toast.LENGTH_SHORT).show()
        }

        binding.btnFindMatch.setOnClickListener {
            // Заглушка
            Toast.makeText(requireContext(), "Поиск совпадений (в разработке)", Toast.LENGTH_SHORT).show()
        }

        binding.btnLeaveRoom.setOnClickListener {
            // Заглушка
            Toast.makeText(requireContext(), "Покинуть комнату (в разработке)", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView() {
        votingAdapter = VotingAdapter(requireContext())
        binding.recyclerViewVoting.adapter = votingAdapter
    }

    private fun setupRoomData(room: Room) {

        with(binding) {
            tvRoomId.text = getString(R.string.tv_room_id, room.roomId.toString())

            tvRoomTitle.text = room.name ?: "Без названия"

            val creator = room.members.find { it.role == MemberRole.CREATOR }
            tvRoomCreator.text = getString(
                R.string.tv_room_creator,
                creator?.username ?: "Неизвестный"
            )

            setupTags(room)
            setupParticipantsAvatars(room.members)
        }
    }


    // =================== ОТОБРАЖЕНИЕ VIEW ЭЛЕМЕНТОВ ===================
    private fun setupTags(room: Room) {
        binding.chipGroupTags.removeAllViews()

        if (!room.expiresAt.isNullOrEmpty()) {
            val formattedDate = formatExpiresAt(room.expiresAt)
            binding.chipGroupTags.addView(createTagChip(formattedDate))
        }

        val votingTypeString = when (room.votingType) {
            com.thedach.kinovod.domain.model.room.VotingType.SINGLE -> "Одиночный"
            com.thedach.kinovod.domain.model.room.VotingType.MULTIPLE -> "Множественный"
            com.thedach.kinovod.domain.model.room.VotingType.PRIORITY -> "Приоритетный"
        }
        binding.chipGroupTags.addView(createTagChip(votingTypeString))

        if (room.votingAnonymous == true) {
            binding.chipGroupTags.addView(createTagChip("Анонимно"))
        }

        room.genres?.forEach { genre ->
            binding.chipGroupTags.addView(createTagChip(genre))
        }

        room.movieType?.forEach { type ->
            val typeString = when (type) {
                0 -> getString(R.string.movie_type_any)
                1 -> getString(R.string.movie_type_movie)
                2 -> getString(R.string.movie_type_series)
                else -> getString(R.string.movie_type_any)
            }
            binding.chipGroupTags.addView(createTagChip(typeString))
        }
    }
    private fun createTagChip(text: String): Chip {
        return Chip(requireContext()).apply {
            this.text = text
            setTextColor(resources.getColor(R.color.white, null))
            chipBackgroundColor = resources.getColorStateList(R.color.purple_500, null)
            textSize = 12f
            isClickable = false
        }
    }
    private fun setupParticipantsAvatars(members: List<RoomMember>) {
        binding.layoutParticipants.removeAllViews()

        val displayMembers = members.take(5)

        for (member in displayMembers) {
            // Создаем FrameLayout как контейнер
            val container = FrameLayout(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    40.dpToPx(),
                    40.dpToPx()
                ).apply {
                    marginEnd = (-8).dpToPx()
                }
            }


            val imageView = ImageView(requireContext()).apply {
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
                scaleType = ImageView.ScaleType.CENTER_CROP

                // Делаем круглым
                clipToOutline = true
                outlineProvider = object : ViewOutlineProvider() {
                    override fun getOutline(view: View, outline: android.graphics.Outline) {
                        outline.setOval(0, 0, view.width, view.height)
                    }
                }
            }

            if (!member.avatar.isNullOrEmpty()) {
                Picasso.get()
                    .load(member.avatar)
                    .into(imageView)
                container.addView(imageView)
            } else {
                val textView = TextView(requireContext()).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                    )
                    gravity = Gravity.CENTER
                    setTextColor(Color.WHITE)
                    textSize = 14f
                    text = member.username.take(2).uppercase()

                    setBackgroundResource(R.drawable.default_avatar_image)

//                    clipToOutline = true
//                    outlineProvider = object : ViewOutlineProvider() {
//                        override fun getOutline(view: View, outline: android.graphics.Outline) {
//                            outline.setOval(0, 0, view.width, view.height)
//                        }
//                    }
                }
                container.addView(textView)
            }

            binding.layoutParticipants.addView(container)
        }
    }
    private fun setupFilters(room: Room) {
        binding.chipGroupFilters.removeAllViews()

        // Добавляем фильтры-чипы
        val filters = listOf(
            "Все фильмы",
            "По рейтингу",
            "По дате",
            "Мои предложения"
        )

        filters.forEach { filter ->
            val chip = Chip(requireContext()).apply {
                text = filter
                setTextColor(resources.getColor(R.color.gray_600, null))
                chipBackgroundColor = resources.getColorStateList(R.color.white, null)
                chipStrokeColor = resources.getColorStateList(R.color.gray_600, null)
                chipStrokeWidth = 1f
                textSize = 13f
                isCheckable = true

                setOnClickListener {
                    // TODO: Применить фильтр
                    Toast.makeText(requireContext(), "Фильтр: $filter", Toast.LENGTH_SHORT).show()
                }
            }
            binding.chipGroupFilters.addView(chip)
        }
    }
    // для конвертации dp в px
    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
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
    // ==================================================================



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}