package com.thedach.kinovod.presentation

import android.app.Application
import com.thedach.kinovod.di.DaggerApplicationComponent

class KinovodApp : Application() {

    val component by lazy {
        DaggerApplicationComponent.builder()
            .build()
    }
}