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

fun String.toDate(): Date? {
    return try {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        format.parse(this)
    } catch (e: ParseException) {
        null
    }
}

fun Date.toFormattedDate(): String {
    val format = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return format.format(this)
}



fun formatToday(): String {
    return java.time.LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH))
}

fun updateAppLanguage(context: Context, language: String) {
    val locale = Locale(language)
    Locale.setDefault(locale)
    val config = Configuration(context.resources.configuration)
    config.setLocale(locale)
    context.resources.updateConfiguration(config, context.resources.displayMetrics)
}

@Composable
fun AppLanguageUpdater(language: String) {
    val context = LocalContext.current
    LaunchedEffect(language) {
        updateAppLanguage(context, language)
    }
}

@Composable
fun RequestLocationPermission(onPermissionGranted: () -> Unit, onPermissionDenied: () -> Unit) {
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onPermissionGranted()
        } else {
            onPermissionDenied()
        }
    }

    LaunchedEffect(Unit) {
        launcher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
    }
}


fun filterByName(query: String, allData: List<DataLeave>): List<DataLeave> {
    return if (query.isNotEmpty()) {
        allData.filter {
            it.userName?.lowercase()?.contains(query.lowercase()) == true ||
                    it.address?.lowercase()?.contains(query.lowercase()) == true
        }
    } else {
        allData
    }
}

@SuppressLint("SimpleDateFormat")
fun filterByDateRange(
    dateRange: Pair<Date?, Date?>,
    allData: List<DataLeave>
): List<DataLeave> {
    val (startDate, endDate) = dateRange
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    return if (startDate != null || endDate != null) {
        allData.filter { dataLeave ->
            val start = dataLeave.startDate?.let { formatApiDateHome(it) }
            val end = dataLeave.endDate?.let { formatApiDateHome(it) }

            val startTime = start?.let { if (it.isNotEmpty()) dateFormat.parse(it) else null }
            val endTime = end?.let { if (it.isNotEmpty()) dateFormat.parse(it) else null }

            (startDate == null || (startTime != null && startTime.after(startDate))) &&
                    (endDate == null || (endTime != null && endTime.before(endDate)))
        }
    } else {
        allData
    }
}

fun formatDate(dateString: String): String {
    // Format input untuk tanggal lengkap (dengan waktu) dan tanggal tanpa waktu
    val inputFormatWithTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("id", "ID"))
    val inputFormatWithoutTime = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))

    // Format output yang diinginkan
    val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))

    // Mencoba untuk parsing dengan kedua format input
    val date: Date? = try {
        // Pertama coba parsing dengan format lengkap (dengan waktu)
        inputFormatWithTime.parse(dateString)
    } catch (e: Exception) {
        try {
            // Jika gagal, coba parsing dengan format tanpa waktu
            inputFormatWithoutTime.parse(dateString)
        } catch (e: Exception) {
            null
        }
    }

    // Jika parsing berhasil, format ulang ke output yang diinginkan
    return date?.let {
        outputFormat.format(it)
    } ?: "Invalid Date"  // Jika parsing gagal, kembalikan "Invalid Date"
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
