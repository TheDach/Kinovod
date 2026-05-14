package com.thedach.kinovod.presentation.room

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.button.MaterialButton
import com.thedach.kinovod.R
import com.thedach.kinovod.data.repository.UserRepository
import com.thedach.kinovod.domain.model.profile.Friend

class FriendsDialogFragment(
    private val selectedFriends: List<Friend>,
    private val onConfirm: (List<Friend>) -> Unit
) : DialogFragment() {

    private val userRepository = UserRepository
    private val tempSelectedFriends = selectedFriends.toMutableList()
    private var friendsList = emptyList<Friend>()

    private lateinit var containerFriends: LinearLayout
    private lateinit var btnCancel: MaterialButton
    private lateinit var btnConfirm: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_friends_selector, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        containerFriends = view.findViewById(R.id.container_friends)
        btnCancel = view.findViewById(R.id.btn_cancel)
        btnConfirm = view.findViewById(R.id.btn_confirm)

        dialog?.setCanceledOnTouchOutside(true)

        loadFriends()
        setupClickListeners()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun setupClickListeners() {
        btnCancel.setOnClickListener {
            dismiss()
        }

        btnConfirm.setOnClickListener {
            onConfirm(tempSelectedFriends.toList())
            dismiss()
        }
    }

    private fun loadFriends() {
        friendsList = userRepository.getUserFriends()
        displayFriends(friendsList)
    }

    private fun displayFriends(friends: List<Friend>) {
        containerFriends.removeAllViews()

        if (friends.isEmpty()) {
            val emptyView = layoutInflater.inflate(
                R.layout.item_empty_state,
                containerFriends,
                false
            )
            containerFriends.addView(emptyView)
            return
        }

        friends.forEach { friend ->
            val friendView = layoutInflater.inflate(
                R.layout.item_friend,
                containerFriends,
                false
            )

            val isSelected = tempSelectedFriends.any { it.userId == friend.userId }

            setupFriendView(friendView, friend, isSelected)

            friendView.setOnClickListener {
                toggleFriendSelection(friendView, friend)
            }

            containerFriends.addView(friendView)
        }
    }
    private fun setupFriendView(view: View, friend: Friend, isSelected: Boolean) {
        val tvFriendName = view.findViewById<TextView>(R.id.tv_friend_name)
        val tvFriendAvatar = view.findViewById<TextView>(R.id.tv_friend_avatar)
        val cardAvatar = view.findViewById<CardView>(R.id.friend_avatar)

        tvFriendName.text = friend.username
        view.setPadding(0, 0, 0, 10)

        // Устанавливаем аватар (первая буква имени)
        val firstLetter = friend.username.firstOrNull()?.uppercase() ?: "?"
        tvFriendAvatar.text = firstLetter

        // Устанавливаем цвет фона аватара
        cardAvatar.background = ContextCompat.getDrawable(requireContext(),R.drawable.default_avatar_image)

        // Устанавливаем стиль выделения
        updateSelectionStyle(view, isSelected)
    }

    private fun updateSelectionStyle(view: View, isSelected: Boolean) {
        val backgroundRes = if (isSelected) {
            R.drawable.bg_gradient_header
        } else {
            R.drawable.bg_gradient_overlay
        }

        view.background = ContextCompat.getDrawable(requireContext(), backgroundRes)

        // Опционально: меняем цвет текста или добавляем галочку
        val tvFriendName = view.findViewById<TextView>(R.id.tv_friend_name)
        if (isSelected) {
            tvFriendName.setTextColor(ContextCompat.getColor(requireContext(), R.color.purple_500))
        } else {
            tvFriendName.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        }
    }

    private fun toggleFriendSelection(view: View, friend: Friend) {
        val isCurrentlySelected = tempSelectedFriends.any { it.userId == friend.userId }

        if (isCurrentlySelected) {
            tempSelectedFriends.removeAll { it.userId == friend.userId }
        } else {
            tempSelectedFriends.add(friend)
        }

        updateSelectionStyle(view, !isCurrentlySelected)
    }

}