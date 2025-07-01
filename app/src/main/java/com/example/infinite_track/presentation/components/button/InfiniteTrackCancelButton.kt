package com.example.infinite_track.presentation.components.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.infinite_track.presentation.core.body1
import com.example.infinite_track.presentation.theme.Blue_200
import com.example.infinite_track.presentation.theme.Blue_500
import com.example.infinite_track.presentation.theme.Infinite_TrackTheme
import com.example.infinite_track.presentation.theme.Violet_50
import com.example.infinite_track.presentation.theme.Violet_500
import com.example.infinite_track.presentation.theme.White


@Composable
fun InfiniteTrackCancelButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    label: String,
    enabled: Boolean = true,
    isOutline: Boolean = false,
) {
    Button(
        onClick = {
            onClick()
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(12.dp),
        colors = if (isOutline) {
            ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White,
                disabledContentColor = Color.White,
                disabledContainerColor = Color.Transparent
            )
        } else {
            ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = Color.White,
                disabledContentColor = Color.Red,
                disabledContainerColor = Color.Red
            )
        },
        border = if (isOutline) BorderStroke(1.dp, Color.Red) else null,
        enabled = enabled,
        elevation = if (isOutline) ButtonDefaults.elevatedButtonElevation(0.dp) else ButtonDefaults.buttonElevation(4.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                style = body1,
                color = if (!isOutline) Color.White else Color.Red
            )
        }
    }
}

@Preview
@Composable
fun InfiniteTrackCancelButtonPreview() {
    Infinite_TrackTheme {
        InfiniteTrackCancelButton(
            onClick = { /* Your action here */ },
            enabled = true,
            label = "Click Me",
            isOutline = false
        )
    }
}
