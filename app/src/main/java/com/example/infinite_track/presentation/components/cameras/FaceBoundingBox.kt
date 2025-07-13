package com.example.infinite_track.presentation.components.cameras

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateRectAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.infinite_track.presentation.screen.attendance.face.LivenessState

// Warna untuk berbagai status liveness
val vibrantBlue = Color(0xFF00A3FF)      // Detecting
val vibrantGreen = Color(0xFF00FFC2)     // Success
val vibrantRed = Color(0xFFFF3B30)       // Failure
val vibrantYellow = Color(0xFFFFD60A)    // Waiting for liveness
val vibrantOrange = Color(0xFFFF9500)    // Liveness detected

/**
 * Komponen untuk menampilkan bounding box deteksi wajah dengan animasi dinamis.
 * @param boundingBox Rect dari wajah yang terdeteksi (Compose UI Rect). Jika null, komponen akan hilang.
 * @param livenessState Status dari proses liveness detection untuk menentukan warna
 * @param strokeWidth Lebar garis sudut.
 */
@Composable
fun FaceBoundingBox(
    modifier: Modifier = Modifier,
    boundingBox: Rect?, // Changed to androidx.compose.ui.geometry.Rect
    livenessState: LivenessState,
    strokeWidth: Dp = 3.dp
) {
    // State untuk animasi saat boundingBox null (menghilang) atau muncul.
    val alpha by animateFloatAsState(
        targetValue = if (boundingBox != null) 1f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "alpha"
    )

    // State untuk animasi posisi dan ukuran
    val animatedBoundingBox by animateRectAsState(
        targetValue = boundingBox ?: Rect.Zero,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "bounds"
    )

    // Tentukan warna berdasarkan LivenessState
    val baseColor = when (livenessState) {
        LivenessState.IDLE -> vibrantBlue
        LivenessState.DETECTING_FACE -> vibrantBlue
        LivenessState.WAITING_FOR_LIVENESS -> vibrantYellow
        LivenessState.LIVENESS_DETECTED -> vibrantOrange
        LivenessState.VERIFYING_FACE -> vibrantOrange
        LivenessState.SUCCESS -> vibrantGreen
        LivenessState.FAILURE -> vibrantRed
        LivenessState.TIMEOUT -> vibrantRed
    }

    // Animasi glow yang berubah berdasarkan status
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowColor by infiniteTransition.animateColor(
        initialValue = baseColor,
        targetValue = if (livenessState == LivenessState.SUCCESS) {
            vibrantGreen
        } else if (livenessState == LivenessState.FAILURE || livenessState == LivenessState.TIMEOUT) {
            vibrantRed.copy(alpha = 0.7f)
        } else {
            baseColor.copy(alpha = 0.8f)
        },
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = if (livenessState == LivenessState.WAITING_FOR_LIVENESS) 1000 else 1500,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowColor"
    )

    val strokeWidthPx = with(LocalDensity.current) { strokeWidth.toPx() }

    if (alpha > 0) {
        Canvas(
            modifier = modifier.fillMaxSize()
        ) {
            // Gambar sudut-sudut penanda dengan warna dinamis
            drawCornerBrackets(
                rect = animatedBoundingBox,
                alpha = alpha,
                strokeWidth = strokeWidthPx,
                baseColor = baseColor,
                glowColor = glowColor,
                livenessState = livenessState
            )
        }
    }
}

private fun DrawScope.drawCornerBrackets(
    rect: Rect,
    alpha: Float,
    strokeWidth: Float,
    baseColor: Color,
    glowColor: Color,
    livenessState: LivenessState
) {
    if (rect.width == 0f || rect.height == 0f) return

    val cornerLength = minOf(rect.width, rect.height) * 0.2f

    // Brush yang berubah berdasarkan status
    val brush = when (livenessState) {
        LivenessState.SUCCESS -> {
            Brush.linearGradient(
                colors = listOf(vibrantGreen, vibrantGreen.copy(alpha = 0.8f)),
                start = rect.topLeft,
                end = rect.bottomRight
            )
        }

        LivenessState.FAILURE, LivenessState.TIMEOUT -> {
            Brush.linearGradient(
                colors = listOf(vibrantRed, vibrantRed.copy(alpha = 0.8f)),
                start = rect.topLeft,
                end = rect.bottomRight
            )
        }

        else -> {
            Brush.linearGradient(
                colors = listOf(baseColor, glowColor),
                start = rect.topLeft,
                end = rect.bottomRight
            )
        }
    }

    // Top-Left Corner
    drawLine(
        brush,
        Offset(rect.left, rect.top + cornerLength),
        rect.topLeft,
        strokeWidth,
        alpha = alpha
    )
    drawLine(
        brush,
        Offset(rect.left + cornerLength, rect.top),
        rect.topLeft,
        strokeWidth,
        alpha = alpha
    )

    // Top-Right Corner
    drawLine(
        brush,
        Offset(rect.right - cornerLength, rect.top),
        rect.topRight,
        strokeWidth,
        alpha = alpha
    )
    drawLine(
        brush,
        Offset(rect.right, rect.top + cornerLength),
        rect.topRight,
        strokeWidth,
        alpha = alpha
    )

    // Bottom-Left Corner
    drawLine(
        brush,
        Offset(rect.left, rect.bottom - cornerLength),
        rect.bottomLeft,
        strokeWidth,
        alpha = alpha
    )
    drawLine(
        brush,
        Offset(rect.left + cornerLength, rect.bottom),
        rect.bottomLeft,
        strokeWidth,
        alpha = alpha
    )

    // Bottom-Right Corner
    drawLine(
        brush,
        Offset(rect.right - cornerLength, rect.bottom),
        rect.bottomRight,
        strokeWidth,
        alpha = alpha
    )
    drawLine(
        brush,
        Offset(rect.right, rect.bottom - cornerLength),
        rect.bottomRight,
        strokeWidth,
        alpha = alpha
    )
}
