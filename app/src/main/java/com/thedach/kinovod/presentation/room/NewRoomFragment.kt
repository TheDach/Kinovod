package com.thedach.kinovod.presentation.room

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.thedach.kinovod.databinding.FragmentNewRoomBinding

class NewRoomFragment: Fragment() {

    private var _binding: FragmentNewRoomBinding? = null
    private val binding: FragmentNewRoomBinding
        get() = _binding ?: throw RuntimeException("FragmentNewRoomBinding == null")

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
    }


    private fun setupClickListeners() {
        binding.btnBackNewRoom.setOnClickListener {
            launchRoomsFragment()
        }
    }

    private fun launchRoomsFragment() {
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}