package com.example.infinite_track.presentation.components.pager

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.example.infinite_track.presentation.components.images.PageIndicator
import com.example.infinite_track.presentation.theme.Infinite_TrackTheme


@Composable
fun PagerIndicatorContent(
    isSelected:Boolean,
    selectedColor: Color,
    defaultColor: Color,
    defaultRadius: Dp,
    selectedRadius: Dp,
    animationDurationMillis: Int,
    modifier: Modifier = Modifier
) {
    val color: Color by animateColorAsState(
        targetValue = if (isSelected) {
            selectedColor
        } else {
            defaultColor
        },
        animationSpec = tween(
            durationMillis = animationDurationMillis
        ), label = ""
    )

    val width: Dp by animateDpAsState(
        targetValue = if (isSelected) {
            selectedRadius
        } else {
            defaultRadius
        },
        animationSpec = tween(
            durationMillis = animationDurationMillis
        ), label = ""
    )

    Canvas(
        modifier = modifier
            .size(
                width = width,
                height = defaultRadius
            )
    ) {
        drawRoundRect(
            color = color,
            topLeft = Offset.Zero,
            size = Size(
                width = width.toPx(),
                height = defaultRadius.toPx(),
            ),
            cornerRadius = CornerRadius(
                x = defaultRadius.toPx(),
                y = defaultRadius.toPx()
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PageIndicatorPreview() {
    Infinite_TrackTheme(dynamicColor = false) {
        PageIndicator(
            numberOfPages = 3,
            selectedPage = 0
        )
    }
}
