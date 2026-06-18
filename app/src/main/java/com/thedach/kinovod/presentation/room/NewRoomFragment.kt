package com.thedach.kinovod.presentation.room

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.thedach.kinovod.R
import com.thedach.kinovod.data.repository.UserRepository
import com.thedach.kinovod.databinding.FragmentNewRoomBinding
import com.thedach.kinovod.domain.model.profile.Friend
import com.thedach.kinovod.domain.model.room.VotingType
import com.thedach.kinovod.presentation.KinovodApp
import com.thedach.kinovod.presentation.ViewModelFactory
import com.thedach.kinovod.presentation.movie.GenresDialogFragment
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class NewRoomFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as KinovodApp).component
    }

    private var _binding: FragmentNewRoomBinding? = null
    private val binding: FragmentNewRoomBinding
        get() = _binding ?: throw RuntimeException("FragmentNewRoomBinding == null")


    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: NewRoomViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[NewRoomViewModel::class.java]
    }

    private val selectedFriends = mutableListOf<Friend>()
    private val selectedGenres = mutableListOf<String>()
    private val selectedMovieTypes = mutableListOf<String>()

    private var currentVotingType = VotingType.SINGLE
    private var votingDeadline = "до завтра 20:00 ⌄"
    private var selectedVotingDeadline: String? = null
    private var moviesLimit = 5


    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewRoomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        setupVotingTypeChips()
        observeViewModel()
    }

    private fun observeViewModel() {

        viewModel.room.observe(viewLifecycleOwner) { room ->
            room?.let {
                launchVotingRoomFragment(room.roomId)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {isLoading ->
            if (isLoading) {
                binding.btnCreateRoom.isEnabled = false
                binding.btnCreateRoom.text = "Создание..."
            } else {
                binding.btnCreateRoom.isEnabled = true
                binding.btnCreateRoom.text = getString(R.string.btn_create_room)
            }
        }
        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), "Ошибка: $it", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnBackNewRoom.setOnClickListener {
            launchRoomsFragment()
        }

        binding.chipAddFriends.setOnClickListener {
            showFriendsDialog()
        }

        binding.chipAddFilter.setOnClickListener {
            showFiltersDialog()
        }
        binding.tvMoviesLimit.setOnClickListener {
            showMoviesLimitDialog()
        }

        binding.tvVotingDeadline.setOnClickListener {
            showVotingDeadlineDialog()
        }
        binding.btnCreateRoom.setOnClickListener {
            createRoom()
        }
    }

    private fun setupVotingTypeChips() {
        binding.chipSingleVote.setOnClickListener {
            updateVotingTypeSelection(VotingType.SINGLE)
        }

        binding.chipMultipleVote.setOnClickListener {
            updateVotingTypeSelection(VotingType.MULTIPLE)
        }

        binding.chipPriorityVote.setOnClickListener {
            updateVotingTypeSelection(VotingType.PRIORITY)
        }
    }

    private fun updateVotingTypeSelection(votingType: VotingType) {
        currentVotingType = votingType

        // Сброс всех чипов
        val chips = listOf(
            binding.chipSingleVote,
            binding.chipMultipleVote,
            binding.chipPriorityVote
        )

        chips.forEach { chip ->
            chip.isChecked = false
            chip.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.text_rating_chips_poster
                )
            )
            chip.setChipBackgroundColorResource(R.color.bg_chip_non_choice)
        }

        // Выбор активного чипа
        when (votingType) {
            VotingType.SINGLE -> {
                binding.chipSingleVote.isChecked = true
                binding.chipSingleVote.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.dark_purple_movie
                    )
                )
                binding.chipSingleVote.setChipBackgroundColorResource(R.color.bg_chip_choice)
            }

            VotingType.MULTIPLE -> {
                binding.chipMultipleVote.isChecked = true
                binding.chipMultipleVote.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.dark_purple_movie
                    )
                )
                binding.chipMultipleVote.setChipBackgroundColorResource(R.color.bg_chip_choice)
            }

            VotingType.PRIORITY -> {
                binding.chipPriorityVote.isChecked = true
                binding.chipPriorityVote.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.dark_purple_movie
                    )
                )
                binding.chipPriorityVote.setChipBackgroundColorResource(R.color.bg_chip_choice)
            }
        }
    }
    private fun showFiltersDialog() {
        val options = listOf("Жанры", "Типы фильмов")

        AlertDialog.Builder(requireContext())
            .setTitle("Добавить фильтр")
            .setItems(options.toTypedArray()) { _, which ->
                when (which) {
                    0 -> showGenresDialog()
                    1 -> showMovieTypesDialog()
                }
            }
            .show()
    }
    private fun showGenresDialog() {
        val dialog = GenresDialogFragment(selectedGenres) { genres ->
            selectedGenres.clear()
            selectedGenres.addAll(genres)
            updateFiltersChips()
        }
        dialog.show(parentFragmentManager, "genres_dialog")
    }
    private fun showMovieTypesDialog() {
        val movieTypes = listOf("Фильм", "Сериал", "Мультфильм", "Аниме", "Документальный")

        val checkedItems = BooleanArray(movieTypes.size) { index ->
            selectedMovieTypes.contains(movieTypes[index])
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Выберите типы фильмов")
            .setMultiChoiceItems(movieTypes.toTypedArray(), checkedItems) { _, which, isChecked ->
                val typeName = movieTypes[which]
                if (isChecked) {
                    if (!selectedMovieTypes.contains(typeName)) {
                        selectedMovieTypes.add(typeName)
                    }
                } else {
                    selectedMovieTypes.remove(typeName)
                }
            }
            .setPositiveButton("OK") { _, _ ->
                updateFiltersChips()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun updateFiltersChips() {
        val chipGroup = binding.chipGroupFilters

        // Сохраняем чип "Добавить фильтр"
        val addFilterChip = chipGroup.findViewById<Chip>(R.id.chip_add_filter)

        // Очищаем все чипы
        chipGroup.removeAllViews()

        // Добавляем чип "Добавить фильтр"
        if (addFilterChip != null) {
            chipGroup.addView(addFilterChip)
        } else {
            val newAddChip =
                layoutInflater.inflate(R.layout.item_chip_add_filter, chipGroup, false) as Chip
            newAddChip.id = R.id.chip_add_filter
            newAddChip.text = getString(R.string.chip_add)
            newAddChip.setOnClickListener {
                showFiltersDialog()
            }
            chipGroup.addView(newAddChip)
        }

        // Добавляем выбранные жанры
        selectedGenres.forEach { genre ->
            val chip = layoutInflater.inflate(R.layout.item_chip_filter, chipGroup, false) as Chip
            chip.id = View.generateViewId()
            chip.text = genre
            chip.isCloseIconVisible = true
            chip.setOnCloseIconClickListener {
                selectedGenres.remove(genre)
                updateFiltersChips()
            }
            chipGroup.addView(chip)
        }

        // Добавляем выбранные типы фильмов
        selectedMovieTypes.forEach { type ->
            val chip = layoutInflater.inflate(R.layout.item_chip_filter, chipGroup, false) as Chip
            chip.id = View.generateViewId()
            chip.text = type
            chip.isCloseIconVisible = true
            chip.setOnCloseIconClickListener {
                selectedMovieTypes.remove(type)
                updateFiltersChips()
            }
            chipGroup.addView(chip)
        }
    }

    private fun showMoviesLimitDialog() {
        val options = arrayOf("3", "5", "10", "15", "20")

        AlertDialog.Builder(requireContext())
            .setTitle("Лимит фильмов на пользователя")
            .setItems(options) { _, which ->
                moviesLimit = options[which].toInt()
                binding.tvMoviesLimit.text = "${options[which]} на пользователей ⌄"
            }
            .show()
    }

    private fun showVotingDeadlineDialog() {
        // Создаем DatePicker
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Выберите дату")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            // Конвертируем выбранную дату из миллисекунд в Calendar
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = selection

            // Показываем TimePicker
            showMaterialTimePicker(calendar)
        }

        datePicker.show(parentFragmentManager, "date_picker")
    }
    private fun showMaterialTimePicker(calendar: Calendar) {
        val timePicker = MaterialTimePicker.Builder()
            .setTitleText("Выберите время")
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(calendar.get(Calendar.HOUR_OF_DAY))
            .setMinute(calendar.get(Calendar.MINUTE))
            .build()

        timePicker.addOnPositiveButtonClickListener {
            val hour = timePicker.hour
            val minute = timePicker.minute

            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, 0) // Обнуляем секунды

            // Сохраняем дату в формате для БД
            selectedVotingDeadline = formatDateTimeForDatabase(calendar)

            // Форматируем для отображения пользователю
            val formattedDateTime = formatDateTime(calendar)
            votingDeadline = "$formattedDateTime ⌄"
            binding.tvVotingDeadline.text = votingDeadline
        }

        timePicker.show(parentFragmentManager, "time_picker")
    }
    private fun formatDateTime(calendar: Calendar): String {
        val currentCalendar = Calendar.getInstance()

        // Проверяем, если дата сегодня
        val isToday = calendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) &&
                calendar.get(Calendar.DAY_OF_YEAR) == currentCalendar.get(Calendar.DAY_OF_YEAR)

        // Проверяем, если дата завтра
        currentCalendar.add(Calendar.DAY_OF_YEAR, 1)
        val isTomorrow = calendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) &&
                calendar.get(Calendar.DAY_OF_YEAR) == currentCalendar.get(Calendar.DAY_OF_YEAR)
        currentCalendar.add(Calendar.DAY_OF_YEAR, -1)

        val timeString = String.format("%02d:%02d",
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE)
        )

        return when {
            isToday -> "сегодня $timeString"
            isTomorrow -> "завтра $timeString"
            else -> {
                val dateFormat = SimpleDateFormat("d MMM", Locale.getDefault())
                "${dateFormat.format(calendar.time)} $timeString"
            }
        }
    }
    private fun formatDateTimeForDatabase(calendar: Calendar): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }


    private fun showFriendsDialog() {
        val dialog = FriendsDialogFragment(selectedFriends) { friends ->
            selectedFriends.clear()
            selectedFriends.addAll(friends)
            updateFriendsChips()
        }
        dialog.show(parentFragmentManager, "friends_dialog")
    }
    private fun updateFriendsChips() {
        // Очищаем существующие чипы друзей, оставляя только чип "Добавить"
        val chipGroup = binding.chipGroupFriends
        val addFriendsChip = chipGroup.findViewById<Chip>(R.id.chip_add_friends)

        chipGroup.removeAllViews()

        if (addFriendsChip != null) {
            chipGroup.addView(addFriendsChip)
        } else {
            // Если чип не найден, создаем новый
            val newAddChip =
                layoutInflater.inflate(R.layout.item_chip_add_friends, chipGroup, false) as Chip
            newAddChip.id = R.id.chip_add_friends
            newAddChip.text = getString(R.string.chip_add_friends)
            newAddChip.setOnClickListener {
                showFriendsDialog()
            }
            chipGroup.addView(newAddChip)
        }

        selectedFriends.forEach { friend ->
            val chip = layoutInflater.inflate(R.layout.item_chip_friend, chipGroup, false) as Chip
            chip.id = View.generateViewId()
            chip.text = friend.username
            chip.isCloseIconVisible = true
            chip.setOnCloseIconClickListener {
                selectedFriends.remove(friend)
                updateFriendsChips()
            }
            chipGroup.addView(chip)
        }
    }

    private fun createRoom() {
        val roomName = binding.editTextRoomName.text.toString()
        if (roomName.isEmpty()) {
            binding.editTextRoomName.error = "Введите название комнаты"
            return
        }

        if (selectedFriends.isEmpty()) {
            Toast.makeText(requireContext(), "Добавьте хотя бы одного участника", Toast.LENGTH_SHORT).show()
            return
        }

        val movieTypeValues = mapOf(
            "Фильм" to 1,
            "Сериал" to 2,
            "Мультфильм" to 3,
            "Аниме" to 4,
            "Документальный" to 5
        )

        val movieTypeInts = selectedMovieTypes.map { movieTypeValues[it] ?: 1 }

        viewModel.createRoom(
            roomName = roomName,
            members = selectedFriends,
            selectedVotingDeadline = selectedVotingDeadline,
            votingType = currentVotingType,
            moviesLimit = moviesLimit,
            movieTypes = movieTypeInts,
            genres = selectedGenres,
            anonymousVoting = binding.switchAnonymousVoting.isChecked,
            compareWishlists = binding.switchCompareWishlists.isChecked
        )
    }

    private fun launchRoomsFragment() {
        findNavController().popBackStack()
    }
    private fun launchVotingRoomFragment(roomId: Int) {
        findNavController().navigate(
            NewRoomFragmentDirections.actionNewRoomFragmentToVotingRoomFragment(roomId)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}