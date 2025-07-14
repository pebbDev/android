package com.example.infinite_track.presentation.components.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.infinite_track.presentation.core.body1
import com.example.infinite_track.presentation.core.body2
import com.example.infinite_track.presentation.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfiniteTrackTextArea(
    modifier: Modifier = Modifier,
    label: String = "Description",
    value: String = "",
    placeholder: String = "",
    enabled: Boolean = true,
    onValueChange: (String) -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = body1,
            color = Purple_700,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = if (placeholder.isNotEmpty()) {
                {
                    Text(
                        text = placeholder,
                        style = body2,
                        color = Purple_400
                    )
                }
            } else null,
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 120.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(
                    color = Violet_50.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(15.dp)
                ),
            shape = RoundedCornerShape(15.dp),
            enabled = enabled,
            singleLine = false,
            textStyle = body2,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedTextColor = Text,
                unfocusedTextColor = Text,
                disabledTextColor = Purple_400
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun InfiniteTrackTextAreaPreview() {
    Infinite_TrackTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Text Area Examples",
                style = body1,
                color = Purple_700
            )

            InfiniteTrackTextArea(
                label = "Description",
                value = "",
                placeholder = "Enter your description here...",
                onValueChange = {}
            )

            InfiniteTrackTextArea(
                label = "Notes",
                value = "Sample text content",
            )
        }
    }

}