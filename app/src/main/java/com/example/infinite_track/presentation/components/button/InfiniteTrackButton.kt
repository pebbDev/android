package com.example.infinite_track.presentation.components.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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

@Composable
fun InfiniteTrackButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    label: String,
    enabled: Boolean = true,
    isOutline: Boolean = false,
    isLoading: Boolean? = null,

) {
    Button(
        onClick = {
            if (isLoading == true) {
                return@Button
            } else {
                onClick()
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(12.dp),
        colors = if (isOutline) {
            ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White,
                disabledContentColor = Blue_200,
                disabledContainerColor = Violet_500
            )
        } else {
            ButtonDefaults.buttonColors(
                containerColor = Blue_500,
                contentColor = Color.White,
                disabledContentColor = Blue_200,
                disabledContainerColor = Violet_500
            )
        },
        border = if (isOutline) BorderStroke(1.dp, Blue_500) else null,
        enabled = enabled,
        elevation = if (isOutline) ButtonDefaults.elevatedButtonElevation(0.dp) else ButtonDefaults.buttonElevation(
            4.dp
        ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                style = body1,
                color = if (!isOutline) Violet_50 else Blue_500
            )
        }
    }
}

@Preview
@Composable
fun InfiniteTrackButtonPriview() {
    Infinite_TrackTheme {
        InfiniteTrackButton(
            onClick = { /* Your action here */ },
            enabled = true,
            label = "Click Me",
            isOutline = true
        )
    }
}
