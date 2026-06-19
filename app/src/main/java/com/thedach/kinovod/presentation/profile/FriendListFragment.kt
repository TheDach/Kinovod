package com.thedach.kinovod.presentation.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.thedach.kinovod.data.repository.UserRepository
import com.thedach.kinovod.databinding.DialogAddFriendsBinding
import com.thedach.kinovod.databinding.FragmentFriendListBinding
import com.thedach.kinovod.presentation.KinovodApp
import com.thedach.kinovod.presentation.ViewModelFactory
import com.thedach.kinovod.presentation.adapters.FriendAdapter
import javax.inject.Inject

class FriendListFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as KinovodApp).component
    }

    @Inject
    lateinit var viewModelFactory:  ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[FriendListViewModel::class.java]
    }

    private var _binding: FragmentFriendListBinding? = null
    private val binding: FragmentFriendListBinding
        get() = _binding ?: throw RuntimeException("FragmentFriendListBinding == null")

    private lateinit var friendAdapter: FriendAdapter


    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFriendListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObserver()
        setupClickListeners()
    }

    private fun setupObserver() {
        UserRepository.currentUser.observe(viewLifecycleOwner) { user ->
            friendAdapter.submitList(user.friends)
        }
        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (!error.isNullOrBlank()) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnAddFriend.setOnClickListener {
            showAddFriendDialog()
        }
    }

    private fun setupRecyclerView() {
        friendAdapter = FriendAdapter(requireActivity())
        binding.recyclerViewFriendList.adapter = friendAdapter
    }

    private fun showAddFriendDialog() {
        val bindingFriendDialog = DialogAddFriendsBinding
            .inflate(LayoutInflater.from(requireContext()))

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(bindingFriendDialog.root)
            .create()

        with(bindingFriendDialog) {
            btnCancel.setOnClickListener {
                dialog.dismiss()
            }
            btnSubmit.setOnClickListener {
                val userTag = etFriendTag.text.toString().trim()
                if (userTag.isEmpty()) {
                    etFriendTag.error = "Введите Tag пользователя"
                    return@setOnClickListener
                }

                dialog.dismiss()
                viewModel.addFriend(userTag)
            }
        }
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}