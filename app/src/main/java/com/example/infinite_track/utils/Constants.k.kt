package com.example.infinite_track.utils

import android.util.Log
import androidx.navigation.NavController
import java.util.Calendar
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

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
