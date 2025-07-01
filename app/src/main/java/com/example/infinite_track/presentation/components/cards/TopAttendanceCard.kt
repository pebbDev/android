package com.example.infinite_track.presentation.components.cards

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.infinite_track.R
import com.example.infinite_track.presentation.core.body1
import com.example.infinite_track.presentation.core.bodyThin
import com.example.infinite_track.presentation.core.headline0
import com.example.infinite_track.presentation.core.headline4
import com.example.infinite_track.presentation.theme.Blue_100
import com.example.infinite_track.presentation.theme.Blue_500
import com.example.infinite_track.presentation.theme.Infinite_TrackTheme
import com.example.infinite_track.presentation.theme.White

@Composable
fun TopAttendanceCard(
    nomor: String,
    modifier: Modifier = Modifier
) {
    // Interaction source to detect press actions
    val mutableInteractionSource = remember { MutableInteractionSource() }
    val pressed = mutableInteractionSource.collectIsPressedAsState()

    // Animated border color
    val borderColor = animateColorAsState(
        targetValue = if (pressed.value) Blue_100 else Color.White,
        label = "BorderColor"
    )

    // Animated background color
    val backgroundColor = animateColorAsState(
        targetValue = if (pressed.value) White else Color.Transparent,
        label = "BackgroundColor"
    )

    val elevation = animateDpAsState(
        targetValue = if (pressed.value) 8.dp else 0.dp,
        label = "Elevation"
    )
    Box(
        modifier = modifier
            .padding(4.dp) // Ensure shadow is not clipped
            .shadow(
                elevation = elevation.value,
                shape = RoundedCornerShape(8.dp),
                clip = false,
                ambientColor = Blue_500.copy(alpha = 0.5f), // Shadow color
                spotColor = Blue_500 // Shadow color when pressed
            )
            .background(
                color = backgroundColor.value,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp, // Border thickness
                color = borderColor.value,
                shape = RoundedCornerShape(8.dp) // Border radius
            )
            .clickable(
                interactionSource = mutableInteractionSource,
                indication = null // Remove ripple effect
            ) { }
            .width(433.dp)
            .height(67.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 15.dp, vertical = 7.dp)
        ) {
            Text(
                text = nomor,
                style = headline0,
                modifier = modifier
                    .padding(top = 6.dp)
                    .width(20.dp)
            )
            AsyncImage(
                model = R.drawable.ic_avatareditprofile,
                contentDescription = "",
                modifier = Modifier
                    .padding(end = 4.dp)
                    .padding(horizontal = 8.dp)
                    .height(50.dp)
                    .width(50.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Column() {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    Text(
                        text = "Raja Muhammad Farhan Zahputra",
                        style = headline4,
                        modifier = Modifier
                            .width(170.dp)
                            .padding(top = 7.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "20 Minutes ago",
                        style = bodyThin,
                    )
                }
                Text(
                    text = "Check In: 10:30",
                    style = body1,
                    color = Color(0xFF8A3DFF)
                )
            }
        }
    }
}

@Preview
@Composable
private fun TopAttendanceCardPreview() {
    Infinite_TrackTheme {
        TopAttendanceCard(nomor = "1")
    }
}