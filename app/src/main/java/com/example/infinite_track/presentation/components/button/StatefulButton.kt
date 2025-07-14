// File: presentation/components/button/StatefulButton.kt

package com.example.infinite_track.presentation.components.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.infinite_track.presentation.core.body1
import com.example.infinite_track.presentation.core.headline4
import com.example.infinite_track.presentation.theme.Blue_500
import com.example.infinite_track.presentation.theme.Blue_700
import com.example.infinite_track.presentation.theme.Blue_Info
import com.example.infinite_track.presentation.theme.Green_Success
import com.example.infinite_track.presentation.theme.Infinite_TrackTheme
import com.example.infinite_track.presentation.theme.Red_Error
import com.example.infinite_track.presentation.theme.Yellow_Warning

enum class ButtonStyle {
    Elevated, Outlined
}

enum class ButtonStateType {
    Default, Success, Info, Warning, Error
}

@Composable
private fun getColorsForState(stateType: ButtonStateType): Pair<Color, Color> {
    return when (stateType) {
        ButtonStateType.Default -> Blue_500 to Color.White
        ButtonStateType.Success -> Green_Success to Color.Black
        ButtonStateType.Info -> Blue_Info to Color.White
        ButtonStateType.Warning -> Yellow_Warning to Color.Black
        ButtonStateType.Error -> Red_Error to Color.White
    }
}

@Composable
fun StatefulButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    style: ButtonStyle,
    stateType: ButtonStateType = ButtonStateType.Default,
    isSelected: Boolean = false
) {
    val (stateColor, contentColorForSelected) = getColorsForState(stateType)

    val buttonModifier = modifier
        .fillMaxWidth()
        .height(48.dp)

    when (style) {
        ButtonStyle.Elevated -> {
            ElevatedButton(
                onClick = onClick,
                modifier = buttonModifier,
                enabled = enabled,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = Blue_500,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = text,
                    style = body1
                )
            }
        }

        ButtonStyle.Outlined -> {
            if (isSelected) {
                // State: Outlined & Selected -> Tampil sebagai tombol Filled
                Button(
                    onClick = onClick,
                    modifier = buttonModifier,
                    enabled = enabled,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = stateColor,
                        contentColor = contentColorForSelected
                    )
                ) {
                    Text(
                        text = text,
                        style = body1
                    )
                }
            } else {
                // State: Outlined & Unselected -> Tampil sebagai OutlinedButton
                OutlinedButton(
                    onClick = onClick,
                    modifier = buttonModifier,
                    enabled = enabled,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = stateColor // Warna teks saat unselected
                    ),
                    border = BorderStroke(
                        1.dp,
                        if (enabled) stateColor else stateColor.copy(alpha = 0.5f)
                    )
                ) {
                    Text(
                        text = text,
                        style = body1
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 320)
@Composable
fun StatefulButton_Previews() {
    Infinite_TrackTheme {
        var isSelected by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Elevated Style",
                style = headline4,
                color = Blue_700
            )
            StatefulButton(text = "Elevated Default", style = ButtonStyle.Elevated, onClick = {})

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Outlined (Unselected)",
                style = headline4,
                color = Blue_700
            )
            StatefulButton(text = "Outlined Default", style = ButtonStyle.Outlined, onClick = {})
            StatefulButton(
                text = "Success State",
                style = ButtonStyle.Outlined,
                stateType = ButtonStateType.Success,
                onClick = {})
            StatefulButton(
                text = "Warning State",
                style = ButtonStyle.Outlined,
                stateType = ButtonStateType.Warning,
                onClick = {})
            StatefulButton(
                text = "Error State",
                style = ButtonStyle.Outlined,
                stateType = ButtonStateType.Error,
                onClick = {})

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Outlined (Selected) - Toggle Me",
                style = headline4,
                color = Blue_700
            )
            StatefulButton(
                text = if (isSelected) "Selected (Success)" else "Unselected (Success)",
                style = ButtonStyle.Outlined,
                stateType = ButtonStateType.Success,
                isSelected = isSelected,
                onClick = { isSelected = !isSelected }
            )
        }
    }
}
