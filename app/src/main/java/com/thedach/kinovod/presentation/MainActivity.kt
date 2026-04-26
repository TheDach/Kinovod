package com.thedach.kinovod.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.thedach.kinovod.R
import com.thedach.kinovod.data.repository.UserRepository
import com.thedach.kinovod.databinding.ActivityMainBinding
import com.thedach.kinovod.presentation.auth.AuthActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding ?: throw RuntimeException("ActivityMainBinding == null")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigationBar()

    }

    private fun setupNavigationBar() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        binding.bottomNavigationViewActivityMain.setupWithNavController(navController)
    }



    override fun onDestroy() {
        super.onDestroy()

        syncDataBeforeClose()

        _binding = null
    }


    private fun syncDataBeforeClose() {
        lifecycleScope.launch {
            try {
                // Принудительная синхронизация всех изменений
                val success = UserRepository.forceSync()
                if (success) {
                    Log.d("MainActivity", "Данные успешно синхронизировались")
                } else {
                    Log.e("MainActivity", "Ошибка синхронизации данных!")
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Не предвиденная ошибка синхронизации", e)
            }
        }
    }

    fun navigateToAuthActivity() {
        startActivity(AuthActivity.newIntent(this))
        finish()
    }



    companion object {

        fun newIntent(
            context: Context
        ) = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }
}