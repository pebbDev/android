package com.example.infinite_track.presentation.components.loading

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LoadingAnimation() {
    val infiniteTransition = rememberInfiniteTransition(label = "Please Waiting. . .")

    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.size(50.dp)
        ) {
            for (i in 1..25) {
                val opacity = 0.02f * (22 - i)
                val offsetAngle = angle - (i * 10f)
                drawArc(
                    color = Color(0xFF7F00FF).copy(alpha = opacity),
                    startAngle = offsetAngle,
                    sweepAngle = 30f,
                    useCenter = false,
                    style = Stroke(width = 35f, cap = StrokeCap.Round) // Garis lengkung
                )
            }

            drawArc(
                color = Color(0xFF7F00FF),
                startAngle = angle,
                sweepAngle = 30f,
                useCenter = false,
                style = Stroke(width = 35f, cap = StrokeCap.Round)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingAnimationPreview() {
    LoadingAnimation()
}
