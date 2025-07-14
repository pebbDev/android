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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.infinite_track.presentation.screen.attendance.face.LivenessState
import com.example.infinite_track.presentation.theme.Blue_500
import com.example.infinite_track.presentation.theme.Blue_Accent_500
import com.example.infinite_track.presentation.theme.Orange_500
import com.example.infinite_track.presentation.theme.Red_Error

// Warna untuk berbagai status liveness menggunakan Color.kt
val vibrantBlue = Blue_500           // Detecting - menggunakan primary blue
val vibrantGreen = Blue_Accent_500   // Success - menggunakan blue accent
val vibrantRed = Red_Error  // Failure - tetap merah untuk kontras yang jelas
val vibrantYellow = Orange_500       // Waiting for liveness - menggunakan orange
val vibrantOrange = Orange_500       // Liveness detected - menggunakan orange

/**
 * Komponen untuk menampilkan bounding box deteksi wajah dengan animasi dinamis.
 * @param boundingBox Rect dari wajah yang terdeteksi (Compose UI Rect). Jika null, komponen akan hilang.
 * @param livenessState Status dari proses liveness detection untuk menentukan warna
 * @param strokeWidth Lebar garis sudut.
 * @param previewSize Size of the camera preview for coordinate scaling
 * @param imageSize Size of the camera image for coordinate scaling
 */
@Composable
fun FaceBoundingBox(
    modifier: Modifier = Modifier,
    boundingBox: Rect?,
    livenessState: LivenessState,
    strokeWidth: Dp = 3.dp,
    previewSize: Size? = null,
    imageSize: Size? = null
) {
    // State untuk animasi saat boundingBox null (menghilang) atau muncul.
    val alpha by animateFloatAsState(
        targetValue = if (boundingBox != null) 1f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "alpha"
    )

    // Calculate scaled bounding box with PROPER coordinate transformation for accurate face tracking
    val scaledBoundingBox = if (boundingBox != null && previewSize != null && imageSize != null) {
        // Calculate scale factors between image and preview
        val scaleX = previewSize.width / imageSize.width
        val scaleY = previewSize.height / imageSize.height

        // Apply coordinate transformation for front camera
        // Front camera is mirrored, so we need to flip X coordinates properly
        val mirroredRect = Rect(
            left = imageSize.width - boundingBox.right,
            top = boundingBox.top,
            right = imageSize.width - boundingBox.left,
            bottom = boundingBox.bottom
        )

        // Scale the mirrored coordinates to preview size
        val transformedRect = Rect(
            left = mirroredRect.left * scaleX,
            top = mirroredRect.top * scaleY,
            right = mirroredRect.right * scaleX,
            bottom = mirroredRect.bottom * scaleY
        )

        // Ensure the bounding box is within preview bounds and return valid rect
        Rect(
            left = transformedRect.left.coerceIn(0f, previewSize.width),
            top = transformedRect.top.coerceIn(0f, previewSize.height),
            right = transformedRect.right.coerceIn(0f, previewSize.width),
            bottom = transformedRect.bottom.coerceIn(0f, previewSize.height)
        )
    } else {
        // Fallback to original bounding box if scaling data is not available
        boundingBox
    }

    // State untuk animasi posisi dan ukuran dengan responsif tracking mengikuti wajah
    val animatedBoundingBox by animateRectAsState(
        targetValue = scaledBoundingBox ?: Rect.Zero,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "bounds"
    )

    // Tentukan warna berdasarkan LivenessState dengan perubahan dinamis
    val baseColor = when (livenessState) {
        LivenessState.IDLE -> vibrantBlue
        LivenessState.DETECTING_FACE -> vibrantBlue
        LivenessState.WAITING_FOR_LIVENESS -> vibrantYellow
        LivenessState.LIVENESS_DETECTED -> vibrantGreen // Langsung hijau saat liveness terdeteksi
        LivenessState.VERIFYING_FACE -> vibrantOrange
        LivenessState.SUCCESS -> vibrantGreen
        LivenessState.FAILURE -> vibrantRed
        LivenessState.TIMEOUT -> vibrantRed
    }

    // Animasi glow yang berubah berdasarkan status dengan feedback visual
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowColor by infiniteTransition.animateColor(
        initialValue = baseColor,
        targetValue = when (livenessState) {
            LivenessState.SUCCESS, LivenessState.LIVENESS_DETECTED -> {
                vibrantGreen.copy(alpha = 0.9f)
            }

            LivenessState.FAILURE, LivenessState.TIMEOUT -> {
                vibrantRed.copy(alpha = 0.7f)
            }

            LivenessState.WAITING_FOR_LIVENESS -> {
                vibrantYellow.copy(alpha = 0.8f)
            }

            else -> {
                baseColor.copy(alpha = 0.8f)
            }
        },
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = when (livenessState) {
                    LivenessState.WAITING_FOR_LIVENESS -> 800 // Faster pulse for waiting
                    LivenessState.LIVENESS_DETECTED -> 400 // Very fast for success feedback
                    else -> 1500
                },
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
            // Gambar sudut-sudut penanda dengan warna dinamis yang mengikuti status
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

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun FaceBoundingBoxPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            FaceBoundingBox(
                boundingBox = Rect(
                    left = 100f,
                    top = 200f,
                    right = 300f,
                    bottom = 400f
                ),
                livenessState = LivenessState.DETECTING_FACE
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun FaceBoundingBoxWaitingPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            FaceBoundingBox(
                boundingBox = Rect(
                    left = 120f,
                    top = 180f,
                    right = 280f,
                    bottom = 380f
                ),
                livenessState = LivenessState.WAITING_FOR_LIVENESS
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun FaceBoundingBoxSuccessPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            FaceBoundingBox(
                boundingBox = Rect(
                    left = 110f,
                    top = 190f,
                    right = 290f,
                    bottom = 390f
                ),
                livenessState = LivenessState.SUCCESS
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun FaceBoundingBoxFailurePreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            FaceBoundingBox(
                boundingBox = Rect(
                    left = 90f,
                    top = 210f,
                    right = 310f,
                    bottom = 410f
                ),
                livenessState = LivenessState.FAILURE
            )
        }
    }
}
