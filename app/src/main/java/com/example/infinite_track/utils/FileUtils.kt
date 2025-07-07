package com.example.infinite_track.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.example.infinite_track.data.soucre.network.response.DataLeave
import java.net.URLEncoder
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun openFile(context: Context, filePath: String) {
    val fileUri = Uri.parse(filePath)
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(fileUri, getMimeType(filePath))
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
    }
    context.startActivity(intent)
}

fun getMimeType(filePath: String): String {
    return when {
        filePath.endsWith(".jpg") || filePath.endsWith(".jpeg") -> "image/jpeg"
        filePath.endsWith(".png") -> "image/png"
        filePath.endsWith(".pdf") -> "application/pdf"
        else -> "*/*"
    }
}

fun updateAppLanguage(context: Context, language: String) {
    val locale = Locale(language)
    Locale.setDefault(locale)
    val config = Configuration(context.resources.configuration)
    config.setLocale(locale)
    context.resources.updateConfiguration(config, context.resources.displayMetrics)
}

fun openWhatsApp(context: Context, phoneNumber: String, messageWA: String) {
    val encodedMessage = URLEncoder.encode(messageWA, "UTF-8")
    val url = "https://wa.me/62$phoneNumber?text=$encodedMessage"

    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "WhatsApp tidak ditemukan", Toast.LENGTH_SHORT).show()
    }
}

fun sendEmail(context: Context, emailAddress: String) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:$emailAddress")
        putExtra(Intent.EXTRA_SUBJECT, "Subjek Email")
        putExtra(Intent.EXTRA_TEXT, "Isi email...")
    }
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Tidak ada aplikasi email", Toast.LENGTH_SHORT).show()
    }
}

fun makePhoneCall(context: Context, phoneNumber: String) {
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:$phoneNumber")
    }
    context.startActivity(intent)
}
