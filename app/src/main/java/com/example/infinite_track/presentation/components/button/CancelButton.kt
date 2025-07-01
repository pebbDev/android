package com.example.infinite_track.presentation.components.button

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
import com.example.infinite_track.presentation.theme.White

@Composable
fun CancelButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    label: String = "Batalkan",
    enabled: Boolean = true
) {
    Button(
        onClick = { onClick() },
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Red,
            contentColor = White,
            disabledContentColor = White.copy(alpha = 0.3f),
            disabledContainerColor = Color.Red.copy(alpha = 0.6f)
        ),
        enabled = enabled,
        elevation = ButtonDefaults.buttonElevation(0.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                style = body1,
                color = Color.White
            )
        }
    }
}

@Preview
@Composable
fun CancelButtonPreview() {
    CancelButton(
        onClick = { /* */ },
        enabled = true,
        label = "Batalkan"
    )
}