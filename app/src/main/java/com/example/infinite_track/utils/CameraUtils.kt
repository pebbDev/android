package com.example.infinite_track.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.infinite_track.R
import com.example.infinite_track.presentation.components.button.OvalIconButton
import com.example.infinite_track.presentation.components.button.customfab.PulsatingCirclesWithIcon
import com.example.infinite_track.presentation.theme.Blue_500


@Composable
fun CameraControls(
    modifier: Modifier = Modifier,
    isImageCaptured: Boolean,
    onCloseClick: () -> Unit,
    onCaptureClick: () -> Unit,
    onConfirmClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Tombol Close / Retake (selalu terlihat)
        OvalIconButton(
            onClick = onCloseClick,
            icon = painterResource(id = R.drawable.ic_close), // Ikon X yang telah dibuat
            contentDescription = "Close or Retake"
        )

        // Tombol Capture (hanya terlihat sebelum gambar diambil)
        AnimatedVisibility(visible = !isImageCaptured, enter = fadeIn(), exit = fadeOut()) {
            Box(modifier = Modifier.size(80.dp), contentAlignment = Alignment.Center) {
                PulsatingCirclesWithIcon()
                FloatingActionButton(
                    onClick = onCaptureClick,
                    containerColor = Blue_500,
                    shape = CircleShape,
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_camera), // Ikon kamera yang telah dibuat
                        contentDescription = "Capture Face",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }

        // Tombol Confirm (hanya terlihat setelah gambar diambil)
        AnimatedVisibility(visible = isImageCaptured, enter = fadeIn(), exit = fadeOut()) {
            OvalIconButton(
                onClick = onConfirmClick,
                icon = painterResource(id = R.drawable.ic_check), // Ikon centang yang telah dibuat
                contentDescription = "Confirm Image"
            )
        }
    }
}