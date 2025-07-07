package com.example.infinite_track.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.navigation.NavController
import java.util.Calendar
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
