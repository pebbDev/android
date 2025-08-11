package com.example.infinite_track

import android.app.Application
import com.example.infinite_track.utils.NotificationHelper
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration

@HiltAndroidApp
class InfiniteTrackApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()

        // Initialize notification channel for Geofencing
        NotificationHelper.createNotificationChannel(this)
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }
}