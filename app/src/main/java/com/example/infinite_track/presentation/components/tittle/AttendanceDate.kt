package com.example.infinite_track.presentation.components.tittle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.infinite_track.presentation.core.body1
import com.example.infinite_track.presentation.theme.Purple_300

@Composable
fun AttendanceDate(
    modifier: Modifier = Modifier,
    date: String,
) {

    Row(
        modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = date,
            style = body1,
            color = Purple_300,
        )
    }
}