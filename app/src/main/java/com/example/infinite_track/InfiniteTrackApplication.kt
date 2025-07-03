package com.example.infinite_track

import android.app.Application
import com.example.infinite_track.utils.NotificationHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class InfiniteTrackApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize notification channel for Geofencing
        NotificationHelper.createNotificationChannel(this)
    }
}