package com.example.infinite_track.presentation.components.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.infinite_track.R
import com.example.infinite_track.presentation.core.body2
import com.example.infinite_track.presentation.core.headline3
import com.example.infinite_track.presentation.theme.Infinite_TrackTheme

@Composable
fun CardAbsence(
    modifier: Modifier = Modifier,
    cardTitle: String,
    cardText: String,
    cardImage: Int,
    onClick: () -> Unit
) {
    var isOnPressed by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .height(80.dp)
            .fillMaxWidth()
    ) {
        // Background blur layer for liquid glass effect
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.2f),
                            Color.White.copy(alpha = 0.1f)
                        )
                    )
                )
                .blur(radius = 8.dp)
        )

        // Glass effect overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.25f),
                            Color.White.copy(alpha = 0.05f)
                        )
                    )
                )
                .drawWithContent {
                    drawContent()
                    // Add glass border highlight
                    drawRect(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.4f),
                                Color.Transparent
                            )
                        ),
                        size = size.copy(height = 2.dp.toPx())
                    )
                    // Side highlight
                    drawRect(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.3f),
                                Color.Transparent
                            )
                        ),
                        size = size.copy(width = 2.dp.toPx())
                    )
                }
        )

        // Main content with transparent background
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(Color.Transparent),
            border = BorderStroke(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.3f),
                        Color.White.copy(alpha = 0.1f)
                    )
                )
            ),
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            isOnPressed = true
                            tryAwaitRelease()
                            isOnPressed = false
                        },
                        onTap = { onClick() }
                    )
                }
                .graphicsLayer {
                    scaleX = if (isOnPressed) 0.98f else 1f
                    scaleY = if (isOnPressed) 0.98f else 1f
                }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp, vertical = 12.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = cardTitle,
                            style = headline3,
                            color = Color.Black.copy(alpha = 0.8f)
                        )
                        Image(
                            painter = painterResource(id = cardImage),
                            contentDescription = "Icon CheckIn",
                            modifier = Modifier.graphicsLayer {
                                alpha = 0.9f
                            }
                        )
                    }
                    Text(
                        text = cardText,
                        color = Color.Gray.copy(alpha = 0.7f),
                        style = body2,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun CardAbsencePreview() {
    Infinite_TrackTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            content = { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                CardAbsence(
                                    cardTitle = "07 : 00",
                                    cardText = "Checked In",
                                    cardImage = R.drawable.ic_checkin,
                                    onClick = { println("Card 1 clicked!") }
                                )
                                CardAbsence(
                                    cardTitle = "10 Day",
                                    cardText = "Absence",
                                    cardImage = R.drawable.ic_absence,
                                    onClick = { println("Card 2 clicked!") }
                                )
                            }
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                CardAbsence(
                                    cardTitle = "15 : 00",
                                    cardText = "Checked Out",
                                    cardImage = R.drawable.ic_checkout,
                                    onClick = { println("Card 3 clicked!") }
                                )
                                CardAbsence(
                                    cardTitle = "15 Day",
                                    cardText = "Total Attended",
                                    cardImage = R.drawable.ic_total_absence,
                                    onClick = { println("Card 4 clicked!") }
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}