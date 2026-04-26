package com.thedach.kinovod.presentation.room

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.thedach.kinovod.R
import com.thedach.kinovod.databinding.FragmentRoomsBinding
import java.lang.RuntimeException

class RoomsFragment: Fragment() {


    private var _binding: FragmentRoomsBinding? = null
    private val binding: FragmentRoomsBinding
        get() = _binding ?: throw RuntimeException("FragmentRoomsBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRoomsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.buttonCreateRooms.setOnClickListener {
            launchNewRoomFragment()
        }
    }


    private fun launchNewRoomFragment() {
        findNavController().navigate(R.id.action_roomsFragment_to_newRoomFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}