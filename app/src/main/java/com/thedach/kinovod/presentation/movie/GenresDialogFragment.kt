package com.thedach.kinovod.presentation.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.chip.Chip
import com.thedach.kinovod.R
import com.thedach.kinovod.data.GenresData
import com.thedach.kinovod.databinding.DialogGenresFilterBinding

class GenresDialogFragment(
    private val selectedGenres: List<String>,
    private val onApply: (List<String>) -> Unit
) : DialogFragment() {

    private var _binding: DialogGenresFilterBinding? = null
    private val binding: DialogGenresFilterBinding
        get() = _binding ?: throw RuntimeException("DialogGenresFilterBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogGenresFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.setCanceledOnTouchOutside(true)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

        val selectedGenresSet = selectedGenres.toMutableSet()

        addAllGenres(selectedGenresSet)
        setupClickListener(selectedGenresSet)
    }

    private fun addAllGenres(selectedGenresSet: MutableSet<String>) {
        GenresData.genres.forEach { genre ->
            val chip = createGenreChip(genre, selectedGenresSet.contains(genre))

            chip.setOnCheckedChangeListener  { _, isChecked ->
                if (isChecked) {
                    selectedGenresSet.add(genre)
                    updateChipStyle(chip, true)
                } else {
                    selectedGenresSet.remove(genre)
                    updateChipStyle(chip, false)
                }
            }

            binding.chipGroupGenres.addView(chip)
        }
    }

    private fun createGenreChip(genre: String, isChecked: Boolean): Chip {
        val chip = layoutInflater.inflate(R.layout.item_chip_genre, binding.chipGroupGenres, false) as Chip
        chip.text = genre
        chip.isChecked = isChecked
        updateChipStyle(chip, isChecked)
        return chip
    }

    private fun updateChipStyle(chip: Chip, isChecked: Boolean) {
        if (isChecked) {
            // Активный чип
            chip.setChipBackgroundColorResource(R.color.purple_500)
            chip.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
        } else {
            // Неактивный чип
            chip.setChipBackgroundColorResource(R.color.white)
            chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        }
    }

    private fun setupClickListener(selectedGenresSet: MutableSet<String>) {
        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnApply.setOnClickListener {
            onApply(selectedGenresSet.toList())
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}