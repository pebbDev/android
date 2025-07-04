package com.example.infinite_track.presentation.screen.attendance.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.infinite_track.presentation.components.button.DualIconButton
import com.example.infinite_track.presentation.theme.Infinite_TrackTheme

@Composable
fun AttendanceTopBar(
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit,
    onFocusLocationClicked: () -> Unit
) {
    DualIconButton(
        modifier = modifier,
        leftIcon = Icons.AutoMirrored.Filled.ArrowBack,
        rightIcon = Icons.Default.MyLocation,
        onLeftClick = onBackClicked,
        onRightClick = onFocusLocationClicked,
        leftContentDescription = "Back",
        rightContentDescription = "Focus Location"
    )
}

@Preview(showBackground = true)
@Composable
fun AttendanceTopBarPreview() {
    Infinite_TrackTheme {
        AttendanceTopBar(
            onBackClicked = { /* Handle back action */ },
            onFocusLocationClicked = { /* Handle focus location action */ }
        )
    }
}
