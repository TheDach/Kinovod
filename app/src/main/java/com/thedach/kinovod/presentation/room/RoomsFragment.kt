package com.thedach.kinovod.presentation.room

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.thedach.kinovod.R
import com.thedach.kinovod.databinding.FragmentRoomsBinding
import com.thedach.kinovod.presentation.adapters.MovieAdapter
import com.thedach.kinovod.presentation.adapters.RoomsAdapter
import java.lang.RuntimeException

class RoomsFragment: Fragment() {


    private var _binding: FragmentRoomsBinding? = null
    private val binding: FragmentRoomsBinding
        get() = _binding ?: throw RuntimeException("FragmentRoomsBinding == null")

    private val viewModel: RoomsViewModel by lazy {
        ViewModelProvider(this)[RoomsViewModel::class.java]
    }

    private lateinit var roomsAdapter: RoomsAdapter

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

        setupRecyclerView()
        setupSwipeRefresh()

        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.buttonCreateRooms.setOnClickListener {
            launchNewRoomFragment()
        }

        roomsAdapter.onRoomClickListener = {
            TODO("переход в VotingRoomFragment")
        }
    }

    private fun observeViewModel() {
        viewModel.roomsList.observe(viewLifecycleOwner) {rooms ->
            roomsAdapter.submitList(rooms)

            if (rooms.isEmpty() && viewModel.roomsList.value?.isNotEmpty() == true) {
                Toast.makeText(requireContext(), "Ничего не найдено", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if(isLoading) {
                binding.progressBarRooms.visibility = View.VISIBLE
            } else {
                binding.progressBarRooms.visibility = View.GONE
            }
        }

        viewModel.isRefreshing.observe(viewLifecycleOwner) {isRefresh ->
            if (isRefresh) {
                binding.swipeRefreshLayoutRooms.isRefreshing = true
            } else {
                binding.swipeRefreshLayoutRooms.isRefreshing = false
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), "Ошибка: $it", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView() {
        roomsAdapter = RoomsAdapter()
        binding.recyclerViewRooms.adapter = roomsAdapter
        binding.recyclerViewRooms.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupSwipeRefresh(){
        binding.swipeRefreshLayoutRooms.setOnRefreshListener {
            viewModel.refreshRooms()
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