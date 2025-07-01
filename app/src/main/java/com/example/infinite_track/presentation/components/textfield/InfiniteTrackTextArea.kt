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
import com.example.infinite_track.presentation.theme.Purple_400
import com.example.infinite_track.presentation.theme.Violet_50
import com.example.infinite_track.presentation.theme.Infinite_TrackTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfiniteTrackTextArea(
    modifier: Modifier = Modifier,
    label: String = "Description",
    value: String = "",
    enabled: Boolean = true,
    onValueChange: (String) -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            color = Purple_400,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        TextField(
            value = value, // Langsung menggunakan String, bukan TextFieldValue
            onValueChange = onValueChange, // Langsung meneruskan callback
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
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                cursorColor = Color.Black
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, widthDp = 320)
@Composable
fun InfiniteTrackTextAreaPreview() {
    Infinite_TrackTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            InfiniteTrackTextArea(
                label = "Leave a Note",
                value = "This is a sample text that shows how the text area will look and behave when filled with content."
            )
        }
    }

}