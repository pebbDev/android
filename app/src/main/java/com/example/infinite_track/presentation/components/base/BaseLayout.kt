package com.example.infinite_track.presentation.components.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.infinite_track.presentation.theme.Blue_500
import com.example.infinite_track.presentation.theme.Blue_Accent_500
import com.example.infinite_track.presentation.theme.Infinite_TrackTheme
import com.example.infinite_track.presentation.theme.Orange_500

@Composable
fun BaseLayout(
    rotation: Float,
    size: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .blur(200.dp)
    ) {
        Box(
            modifier = Modifier
                .offset(x = 0.dp, y = 250.dp)
                .rotate(rotation)
        ) {
            CircleShapeBox(
                modifier = Modifier.offset(x = (-150).dp, y = (-300).dp),
                size = size,
                color = Blue_500
            )
            CircleShapeBox(
                modifier = Modifier.offset(x = (150).dp, y = (0).dp),
                size = size,
                color = Orange_500
            )
            CircleShapeBox(
                modifier = Modifier.offset(x = (-150).dp, y = 300.dp),
                size = size,
                color = Blue_Accent_500
            )
        }
    }
}

@Composable
fun CircleShapeBox(modifier: Modifier, size: Float, color: Color) {
    val defaultSize = 425.dp
    val enlargedSize = 450.dp

    Box(
        modifier = modifier
            .clip(CircleShape)
            .size((defaultSize + (enlargedSize - defaultSize) * size))
            .background(color)
    ) {}
}

@Preview(showSystemUi = true)
@Composable
fun BaseLayoutPreview() {
    Infinite_TrackTheme {
        BaseLayout(
            rotation = 45f,
            size = 0.5f
        )
    }
}