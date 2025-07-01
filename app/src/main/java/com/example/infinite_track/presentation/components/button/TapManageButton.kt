package com.example.infinite_track.presentation.components.button

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.infinite_track.presentation.core.body1
import com.example.infinite_track.presentation.core.body2
import com.example.infinite_track.presentation.theme.Blue_50
import com.example.infinite_track.presentation.theme.Blue_500

@Composable
fun TabManageButton(
    onStatusSelected: (String) -> Unit,
    pendingCount: Int,
    declineCount: Int,
    approveCount: Int
) {
    var button1active by remember { mutableStateOf(1) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .width(327.dp)
            .height(36.dp)
            .border(
                width = 1.dp,
                color = Color.White,
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(10.dp))
            .background(color = Color(0x33FFFFFF))
            .padding(horizontal = 4.dp, vertical = 4.dp)
    ) {
        Button(
            modifier = Modifier
                .width(106.dp)
                .height(30.dp),
            contentPadding = PaddingValues(horizontal = 7.dp),
            onClick = {
                button1active = 1
                onStatusSelected("Pending") // Notify parent composable
            },
            shape = RoundedCornerShape(6.dp),
            colors = ButtonDefaults.buttonColors(
                if (button1active == 1) Blue_500 else Color(0x00FFFFFF)
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pending",
                    style = body1,
                    color = if (button1active == 1) Blue_50 else Blue_500
                )
                Box(
                    modifier = Modifier
                        .width(32.dp)
                        .height(17.dp)
                        .clip(RoundedCornerShape(11.dp))
                        .background(if (button1active == 1) Blue_50 else Blue_500),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$pendingCount",
                        style = body2,
                        color = if (button1active == 1) Blue_500 else Blue_50
                    )
                }
            }
        }
        Button(
            modifier = Modifier
                .width(106.dp)
                .height(30.dp),
            contentPadding = PaddingValues(horizontal = 7.dp),
            onClick = {
                button1active = 2
                onStatusSelected("Decline") // Notify parent composable
            },
            shape = RoundedCornerShape(6.dp),
            colors = ButtonDefaults.buttonColors(
                if (button1active == 2) Blue_500 else Color(0x00FFFFFF)
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Decline",
                    style = body1,
                    color = if (button1active == 2) Blue_50 else Blue_500
                )
                Box(
                    modifier = Modifier
                        .width(32.dp)
                        .height(17.dp)
                        .clip(RoundedCornerShape(11.dp))
                        .background(if (button1active == 2) Blue_50 else Blue_500),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$declineCount",
                        style = body2,
                        color = if (button1active == 2) Blue_500 else Blue_50
                    )
                }
            }
        }
        Button(
            modifier = Modifier
                .width(106.dp)
                .height(30.dp),
            contentPadding = PaddingValues(horizontal = 7.dp),
            onClick = {
                button1active = 3
                onStatusSelected("Approve") // Notify parent composable
            },
            shape = RoundedCornerShape(6.dp),
            colors = ButtonDefaults.buttonColors(
                if (button1active == 3) Blue_500 else Color(0x00FFFFFF)
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Approved",
                    style = body1,
                    color = if (button1active == 3) Blue_50 else Blue_500
                )
                Box(
                    modifier = Modifier
                        .width(32.dp)
                        .height(17.dp)
                        .clip(RoundedCornerShape(11.dp))
                        .background(if (button1active == 3) Blue_50 else Blue_500),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$approveCount",
                        style = body2,
                        color = if (button1active == 3) Blue_500 else Blue_50,
                    )
                }
            }
        }
    }
}
