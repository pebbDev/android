//package com.example.infinite_track.presentation.fcm
//
//import android.util.Log
//import com.google.firebase.messaging.FirebaseMessagingService
//import com.google.firebase.messaging.RemoteMessage
//import com.example.infinite_track.utils.NotificationHelper
//
//class InfiniteTrackFCMService : FirebaseMessagingService() {
//
//    private val TAG = "FCMService"
//
//    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//        super.onMessageReceived(remoteMessage)
//        Log.d(TAG, "From: ${remoteMessage.from}")
//
//        remoteMessage.notification?.let {
//            Log.d(TAG, "Message Notification Body: ${it.body}")
//            // Menampilkan notifikasi yang diterima dari server
//            it.body?.let { message ->
//                NotificationHelper.showGeofenceNotification(applicationContext, message)
//            }
//        }
//    }
//
//    override fun onNewToken(token: String) {
//        super.onNewToken(token)
//        Log.d(TAG, "Refreshed token: $token")
//        // TODO: Kirim token ini ke backend server Anda untuk didaftarkan
//    }
//}
