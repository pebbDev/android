package com.example.infinite_track.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.navigation.NavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

fun showToast(context: Context, @StringRes messageResId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, context.getString(messageResId), duration).show()
}

fun getCurrentDate(): String {
    val calendar = Calendar.getInstance()

    val dayOfWeek = when (calendar.get(Calendar.DAY_OF_WEEK)) {
        Calendar.MONDAY -> "Monday"
        Calendar.TUESDAY -> "Tuesday"
        Calendar.WEDNESDAY -> "Wednesday"
        Calendar.THURSDAY -> "Thursday"
        Calendar.FRIDAY -> "Friday"
        Calendar.SATURDAY -> "Saturday"
        Calendar.SUNDAY -> "Sunday"
        else -> ""
    }

    val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

    val month = when (calendar.get(Calendar.MONTH)) {
        Calendar.JANUARY -> "January"
        Calendar.FEBRUARY -> "February"
        Calendar.MARCH -> "March"
        Calendar.APRIL -> "April"
        Calendar.MAY -> "May"
        Calendar.JUNE -> "June"
        Calendar.JULY -> "July"
        Calendar.AUGUST -> "August"
        Calendar.SEPTEMBER -> "September"
        Calendar.OCTOBER -> "October"
        Calendar.NOVEMBER -> "November"
        Calendar.DECEMBER -> "December"
        else -> ""
    }

    val year = calendar.get(Calendar.YEAR)

    return "$dayOfWeek, $dayOfMonth $month $year"
}

fun formatAttendanceDate(attendanceDate: String?): Pair<String, String> {
    if (attendanceDate.isNullOrEmpty()) return "" to ""

    return try {
        val inputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)
        val date = inputFormat.parse(attendanceDate)

        val dayFormat = SimpleDateFormat("dd", Locale.ENGLISH)
        val day = dayFormat.format(date!!)

        val monthYearFormat = SimpleDateFormat("EEE yyyy", Locale.ENGLISH)
        val monthYear = monthYearFormat.format(date).uppercase(Locale.getDefault())

        day to monthYear
    } catch (e: Exception) {
        e.printStackTrace()
        "" to ""
    }
}

@SuppressLint("DefaultLocale")
fun calculateTotalCourse(checkIn: String?, checkOut: String?): String {
    if (checkIn.isNullOrEmpty() || checkOut.isNullOrEmpty()) {
        return "--:--"
    }

    try {
        val checkInParts = checkIn.split(".").map { it.toInt() }
        val checkOutParts = checkOut.split(".").map { it.toInt() }

        if (checkInParts.size < 2 || checkOutParts.size < 2) {
            return "--:--"
        }

        val checkInMinutes = checkInParts[0] * 60 + checkInParts[1]
        val checkOutMinutes = checkOutParts[0] * 60 + checkOutParts[1]

        val totalMinutes = if (checkOutMinutes < checkInMinutes) {
            checkOutMinutes + (24 * 60) - checkInMinutes
        } else {
            checkOutMinutes - checkInMinutes
        }

        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60

        return String.format("%02d:%02d", hours, minutes)
    } catch (e: Exception) {
        e.printStackTrace()
        return "--:--"
    }
}


fun calculateNextResetTime(lastCheckoutTime: Long): Long {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = lastCheckoutTime
        add(Calendar.DAY_OF_YEAR, 1)
        set(Calendar.HOUR_OF_DAY, 7)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    return calendar.timeInMillis
}

fun calculateDistance(lat1: Float, lon1: Float, lat2: Float, lon2: Float): Float {
    val radiusEarth = 6371000f
    val dLat = Math.toRadians((lat2 - lat1).toDouble()).toFloat()
    val dLon = Math.toRadians((lon2 - lon1).toDouble()).toFloat()
    val a = sin(dLat / 2).pow(2) + cos(Math.toRadians(lat1.toDouble())).toFloat() * cos(
        Math.toRadians(lat2.toDouble())
    ).toFloat() * sin(dLon / 2).pow(2)
    val c = 2 * atan2(sqrt(a.toDouble()), sqrt(1 - a.toDouble())).toFloat()
    return radiusEarth * c
}


fun NavController.safeNavigate(route: String) {
    try {
        navigate(route) {
            popUpTo(graph.startDestinationId) { saveState = true }
            restoreState = true
            launchSingleTop = true
        }
    } catch (e: IllegalArgumentException) {
        Log.e("NavigationError", "Route $route not found in NavGraph: ${e.message}")
    }
}


@SuppressLint("MissingPermission")
fun fetchLocation(context: Context, onLocationFetched: (String) -> Unit) {
    val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    try {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)

                if (!addresses.isNullOrEmpty()) {
                    val city = addresses[0].locality ?: "Unknown City"
                    val area = addresses[0].featureName ?: "Unknown Area"
                    onLocationFetched("$area, $city")
                } else {
                    onLocationFetched("Unknown Location")
                }
            } else {
                onLocationFetched("Unable to fetch location")
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        onLocationFetched("Error fetching location")
    }
}

fun formatApiDate(apiDate: String?): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = inputFormat.parse(apiDate ?: "")
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        ""
    }
}

fun formatApiDateHome(apiDate: String?): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat =
            SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) // Format yang diinginkan
        val date = inputFormat.parse(apiDate ?: "")
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        ""
    }
}


fun Uri.toMultipartBodyPart(partName: String, context: Context): MultipartBody.Part? {
    val contentResolver = context.contentResolver
    val inputStream = contentResolver.openInputStream(this) ?: return null
    val fileName = this.lastPathSegment?.split("/")?.lastOrNull()?.let {
        if (it.endsWith(".jpg", true) || it.endsWith(".jpeg", true)) it else "$it.jpg"
    } ?: "image_${System.currentTimeMillis()}.jpg"
    val file = File(context.cacheDir, fileName)

    file.outputStream().use { inputStream.copyTo(it) }

    val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())

    return MultipartBody.Part.createFormData(partName, file.name, requestBody)
}
