package com.example.infinite_track.presentation.components.tittle

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.infinite_track.presentation.core.headline0
import kotlinx.coroutines.delay
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun LocalTimeText() {
    var currentTime by remember { mutableStateOf(LocalTime.now()) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = LocalTime.now()
            delay(1000L)
        }
    }

    val timeText = currentTime.format(DateTimeFormatter.ofPattern("hh:mm a"))

    Text(
        text = timeText,
        style = headline0,
        modifier = Modifier,
        fontWeight = FontWeight.Medium
    )
}