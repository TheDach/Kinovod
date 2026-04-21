package com.thedach.kinovod.presentation.auth

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.thedach.kinovod.R
import com.thedach.kinovod.databinding.FragmentRegistrationBinding
import java.lang.RuntimeException

class RegistrationFragment: Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding: FragmentRegistrationBinding
        get() = _binding ?: throw RuntimeException("FragmentRegistrationBinding == null")

    private val viewModel: RegistrationViewModel by lazy {
        ViewModelProvider(this)[RegistrationViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewModel() {

        viewModel.isRegister.observe(viewLifecycleOwner) { isRegister ->
            if (isRegister) {
                launchMovieFragment()
            }
        }
        viewModel.error.observe(viewLifecycleOwner) {error ->
            error?.let {
                Toast.makeText(requireContext(), "Ошибка: $it", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupClickListeners() {
        with(binding) {
            btnLogin.setOnClickListener {
                launchLoginFragment()
            }
            btnSwitchToRegister.setOnClickListener {
                launchLoginFragment()
            }
            btnRegister.setOnClickListener {
                val username = textInputEditTextUsernameRegistration.text.toString().trim()
                val email = textInputEditTextEmailRegistration.text.toString().trim()
                val password = textInputEditTextPasswordRegistration.text.toString()

                if (validateInputs(username, email, password)) {
                    viewModel.registerNewUser(username, email, password)
                }
            }
        }

    }

    private fun launchLoginFragment() {
        findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
    }
    private fun launchMovieFragment() {
        if (activity is AuthActivity) {
            (activity as AuthActivity).navigateToMainActivity()
        }
    }

    private fun validateInputs(username: String, email: String, password: String): Boolean {
        var isValid = true

        // Валидация username
        if (username.isBlank()) {
            showErrorForField(binding.textInputLayoutUsername, "Имя пользователя обязательно")
            isValid = false
        } else if (username.length < 3) {
            showErrorForField(binding.textInputLayoutUsername, "Имя пользователя должно содержать минимум 3 символа")
            isValid = false
        } else {
            clearErrorForField(binding.textInputLayoutUsername)
        }

        // Валидация email
        if (email.isBlank()) {
            showErrorForField(binding.textInputLayoutEmail, "Email обязателен")
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showErrorForField(binding.textInputLayoutEmail, "Введите корректный email")
            isValid = false
        } else {
            clearErrorForField(binding.textInputLayoutEmail)
        }

        // Валидация password
        if (password.isBlank()) {
            showErrorForField(binding.textInputLayoutPassword, "Пароль обязателен")
            isValid = false
        } else if (password.length < 6) {
            showErrorForField(binding.textInputLayoutPassword, "Пароль должен содержать минимум 6 символов")
            isValid = false
        } else if (!password.matches(Regex(".*[A-Z].*"))) {
            showErrorForField(binding.textInputLayoutPassword, "Пароль должен содержать хотя бы одну заглавную букву")
            isValid = false
        } else if (!password.matches(Regex(".*\\d.*"))) {
            showErrorForField(binding.textInputLayoutPassword, "Пароль должен содержать хотя бы одну цифру")
            isValid = false
        } else {
            clearErrorForField(binding.textInputLayoutPassword)
        }

        return isValid
    }

    private fun showErrorForField(textInputLayout: TextInputLayout, message: String) {
        textInputLayout.isErrorEnabled = true
        textInputLayout.error = message
        textInputLayout.requestFocus()
    }

    private fun clearErrorForField(textInputLayout: TextInputLayout) {
        textInputLayout.isErrorEnabled = false
        textInputLayout.error = null
    }

    private fun showError(message: String) {
        // Можно показать Snackbar или Toast
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }
}