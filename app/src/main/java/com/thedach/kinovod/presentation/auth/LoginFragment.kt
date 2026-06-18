package com.thedach.kinovod.presentation.auth

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.thedach.kinovod.R
import com.thedach.kinovod.databinding.FragmentLoginBinding
import com.thedach.kinovod.presentation.KinovodApp
import com.thedach.kinovod.presentation.ViewModelFactory
import javax.inject.Inject

class LoginFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as KinovodApp).component
    }

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding
        get() = _binding ?: throw RuntimeException("FragmentLoginBinding == null")

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
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
        viewModel.isLogin.observe(viewLifecycleOwner) { isLogin ->
            if (isLogin) {
                launchMovieFragment()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), "Ошибка: $it", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnRegister.setOnClickListener {
            launchRegistrationFragment()
        }
        binding.btnSwitchToRegister.setOnClickListener {
            launchRegistrationFragment()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.textInputEditTextEmail.text.toString().trim()
            val password = binding.textInputEditTextPassword.text.toString()

            if (validateInputs(email, password)) {
                viewModel.loginUser(email, password)
            }
        }
    }

    /**
     * Валидация полей ввода для логина
     */
    private fun validateInputs(email: String, password: String): Boolean {
        var isValid = true

        // Валидация email
        if (email.isBlank()) {
            showErrorForField(binding.textInputLayoutEmailLogin, "Email обязателен")
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showErrorForField(binding.textInputLayoutEmailLogin, "Введите корректный email")
            isValid = false
        } else {
            clearErrorForField(binding.textInputLayoutEmailLogin)
        }

        // Валидация password
        if (password.isBlank()) {
            showErrorForField(binding.textInputLayoutPasswordLogin, "Пароль обязателен")
            isValid = false
        } else if (password.length < 6) {
            showErrorForField(
                binding.textInputLayoutPasswordLogin,
                "Пароль должен содержать минимум 6 символов"
            )
            isValid = false
        } else {
            clearErrorForField(binding.textInputLayoutPasswordLogin)
        }

        return isValid
    }

    private fun showErrorForField(textInputLayout: TextInputLayout, message: String) {
        textInputLayout.isErrorEnabled = true
        textInputLayout.error = message
        textInputLayout.requestFocus()
    }

    /**
     * Очищает ошибку для конкретного поля ввода
     */
    private fun clearErrorForField(textInputLayout: TextInputLayout) {
        textInputLayout.isErrorEnabled = false
        textInputLayout.error = null
    }


    private fun launchRegistrationFragment() {
        findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
    }

    private fun launchMovieFragment() {
        if (activity is AuthActivity) {
            (activity as AuthActivity).navigateToMainActivity()
        }
    }
}