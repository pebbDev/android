package com.example.infinite_track.presentation.components.button.customfab

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.infinite_track.presentation.theme.Infinite_TrackTheme

@Composable
fun PulsatingCirclesWithIcon(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition()
    val size by infiniteTransition.animateValue(
        initialValue = 80.dp,
        targetValue = 64.dp,
        Dp.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val smallCircle by infiniteTransition.animateValue(
        initialValue = 72.dp,
        targetValue = 64.dp,
        Dp.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(Color(0xFFC9A6FF).copy(alpha = 0.50f))
        )

        Box(
            modifier = Modifier
                .size(smallCircle)
                .clip(CircleShape)
                .background(Color(0xFFB17DFF).copy(alpha = 0.50f))
        )
    }
}



@Preview
@Composable
fun PulsatingCirclesWithIconPreview(){
    Infinite_TrackTheme {
        PulsatingCirclesWithIcon()
    }
}
