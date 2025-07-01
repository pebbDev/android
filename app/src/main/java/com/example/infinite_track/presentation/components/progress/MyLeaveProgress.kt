package com.example.infinite_track.presentation.components.progress

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.infinite_track.R
import com.example.infinite_track.presentation.core.body1

@Composable
fun MyLeaveProgress(
    steps: List<String>,
    activeStep: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        steps.forEachIndexed { index, step ->
            val isAccepted = index < activeStep
            val isHold = index == activeStep

            ProgressItem(
                title = step,
                isAccepted = isAccepted,
                isHold = isHold,
            )
        }
    }
}

@Composable
fun ProgressItem(
    title: String,
    isAccepted: Boolean,
    isHold: Boolean,
) {
    val borderColor = when {
        isAccepted -> Color(0xFF4CAF50)
        isHold -> Color(0xFFFDBA13)
        else -> Color.Gray
    }

    val iconColor = when {
        isAccepted -> Color(0xFF4CAF50)
        isHold -> Color(0xFFFDBA13)
        else -> Color.Gray
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(48.dp)
                .border(2.dp, borderColor, CircleShape)
                .background(
                    color = Color.Transparent,
                    shape = CircleShape
                )
        ) {
            val icon: Painter = if (title == "Approve") {
                painterResource(R.drawable.ic_approved)
            } else {
                painterResource(R.drawable.ic_docs)
            }

            Image(
                painter = icon,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(iconColor)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            style = body1,
            fontSize = 14.sp,
            color = Color.Gray
        )
    }

    if (title != "Approve") {
        Spacer(modifier = Modifier.width(8.dp))
        Canvas(
            modifier = Modifier
                .height(4.dp)
                .padding(vertical = 24.dp)
        ) {
            drawRoundRect(
                color = if (isAccepted) Color(0xFF4CAF50) else Color.Gray,
                topLeft = Offset(0f, 0f),
                size = Size(size.width, 4.dp.toPx()),
                cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
    }
}

@Composable
@Preview
fun ProgressMyLeavePreview() {
    val steps = listOf("Sign", "Pending", "Requested", "Approve")
    MyLeaveProgress(
        steps = steps,
        activeStep = 1
    )
}


