package com.example.infinite_track.presentation.components.cards

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.infinite_track.presentation.core.body1
import com.example.infinite_track.presentation.core.body2
import com.example.infinite_track.presentation.core.headline0
import com.example.infinite_track.presentation.theme.Blue_500

@Composable
fun OverviewCardAttendance(
    modifier: Modifier = Modifier,
    title: String,
    count: Int,
    unit: String,
    onClick: () -> Unit,
) {
    var isOnPressed by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isOnPressed -> Color(0xFFFFFFFF)
                else -> Color(0x33FFFFFF)
            }
        ),
        border = BorderStroke(1.dp, Color(0xFFFFFFFF)),
        modifier = modifier
            .then(
                if (isOnPressed) {
                    Modifier
                        .shadow(
                            elevation = 7.dp,
                            shape = RoundedCornerShape(12.dp),
                            ambientColor = Blue_500,
                            spotColor = Blue_500
                        )
                } else {
                    Modifier
                }
            )
            .clickable {
                isOnPressed = !isOnPressed
                onClick()
            }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = body1,
            )
            Spacer(modifier.height(8.dp))
            Row(verticalAlignment = Alignment.Top) {
                Text(
                    text = count.toString(),
                    style = headline0,
                    color = Blue_500,
                )
                Spacer(modifier.width(4.dp))
                Text(
                    text = unit,
                    style = body2,
                    color = Color.Gray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InfoCardPreview() {
    OverviewCardAttendance(
        title = "Work From Home",
        count = 2,
        unit = "Days",
        onClick = {
            Log.d("AttendanceOverview", "Clicked on")
        }
    )
}

