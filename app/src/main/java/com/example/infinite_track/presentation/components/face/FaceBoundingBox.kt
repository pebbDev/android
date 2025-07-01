package com.example.infinite_track.presentation.components.face

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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
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

// Warna utama untuk bounding box
val vibrantBlue = Color(0xFF00A3FF)
val vibrantGreen = Color(0xFF00FFC2)

/**
 * Komponen untuk menampilkan bounding box deteksi wajah dengan animasi.
 * @param boundingBox Rect dari wajah yang terdeteksi. Jika null, komponen akan hilang.
 * @param strokeWidth Lebar garis sudut.
 */
@Composable
fun FaceBoundingBox(
    modifier: Modifier = Modifier,
    boundingBox: Rect?,
    strokeWidth: Dp = 3.dp
) {
    // State untuk animasi saat boundingBox null (menghilang) atau muncul.
    // Animasi alpha (fade in/out).
    val alpha by animateFloatAsState(
        targetValue = if (boundingBox != null) 1f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "alpha"
    )

    // State untuk animasi posisi dan ukuran. Menggunakan spring untuk efek 'membal'.
    val animatedBoundingBox by animateRectAsState(
        targetValue = boundingBox ?: Rect.Zero, // Kembali ke (0,0) saat hilang
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "bounds"
    )

    // State untuk animasi kilauan (glow) yang berulang
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowColor by infiniteTransition.animateColor(
        initialValue = vibrantBlue,
        targetValue = vibrantGreen,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "glowColor"
    )

    val strokeWidthPx = with(LocalDensity.current) { strokeWidth.toPx() }

    if (alpha > 0) {
        Canvas(
            modifier = modifier
                .fillMaxSize()
        ) {
            // Gambar sudut-sudut penanda
            drawCornerBrackets(
                rect = animatedBoundingBox,
                alpha = alpha,
                strokeWidth = strokeWidthPx,
                glowColor = glowColor
            )
        }
    }
}

private fun DrawScope.drawCornerBrackets(
    rect: Rect,
    alpha: Float,
    strokeWidth: Float,
    glowColor: Color
) {
    if (rect.width == 0f || rect.height == 0f) return

    val cornerLength = minOf(rect.width, rect.height) * 0.2f // Panjang setiap garis sudut

    // Brush untuk efek gradasi/glow
    val brush = Brush.linearGradient(
        colors = listOf(vibrantBlue, glowColor),
        start = rect.topLeft,
        end = rect.bottomRight
    )

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


@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun FaceBoundingBoxPreview() {
    // Contoh penggunaan dengan bounding box statis untuk preview
    Box(modifier = Modifier.size(300.dp)) {
        FaceBoundingBox(
            boundingBox = Rect(
                offset = Offset(100f, 150f),
                size = Size(300f, 400f)
            ),
            modifier = Modifier.fillMaxSize()
        )
    }
}