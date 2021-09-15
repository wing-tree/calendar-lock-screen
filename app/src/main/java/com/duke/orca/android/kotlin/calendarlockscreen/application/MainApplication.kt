package com.duke.orca.android.kotlin.calendarlockscreen.application

import android.app.Application
import android.icu.util.Calendar
import androidx.viewbinding.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    companion object {
        val today = Calendar.getInstance().get(Calendar.JULIAN_DAY)
    }
}