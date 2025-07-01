package com.example.infinite_track.presentation.components.button.attendance

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.infinite_track.presentation.components.button.RadioButtonWithText
import com.example.infinite_track.presentation.theme.Infinite_TrackTheme

/**
 * Komponen untuk memilih mode kerja antara Work From Home dan Work From Anywhere.
 *
 * @param selectedMode Mode yang sedang dipilih ("WFH" atau "WFA")
 * @param onModeSelected Callback yang dipanggil ketika mode dipilih
 */
@Composable
fun WorkModeSelector(
    selectedMode: String,
    onModeSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        RadioButtonWithText(
            text = "Work From Home",
            selected = selectedMode == "WFH",
            onClick = { onModeSelected("WFH") }
        )

        RadioButtonWithText(
            text = "Work From Anywhere",
            selected = selectedMode == "WFA",
            onClick = { onModeSelected("WFA") }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WorkModeSelectorPreview() {
    Infinite_TrackTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            WorkModeSelector(
                selectedMode = "WFH",
                onModeSelected = {}
            )

            WorkModeSelector(
                selectedMode = "WFA",
                onModeSelected = {}
            )
        }
    }
}
