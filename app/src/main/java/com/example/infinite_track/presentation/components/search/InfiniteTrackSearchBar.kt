package com.example.infinite_track.presentation.components.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.infinite_track.presentation.theme.Infinite_TrackTheme
import com.example.infinite_track.presentation.theme.Purple_300
import com.example.infinite_track.presentation.theme.Purple_500
import com.example.infinite_track.presentation.theme.Violet_50

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfiniteTrackSearchBar(
    modifier: Modifier = Modifier,
    value: String = "",
    placeholder: String = "Search...",
    onChange: (String) -> Unit = {},
) {
    var searchValue by remember { mutableStateOf(value) }

    OutlinedTextField(
        value = searchValue,
        onValueChange = { newValue ->
            searchValue = newValue
            onChange(newValue)
        },
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(Violet_50.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(15.dp),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                tint = Purple_500,
                contentDescription = "Search Event"
            )
        },
        placeholder = {
            Text(
                placeholder,
                color = Purple_300
            )
        },
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Purple_500,
            unfocusedBorderColor = Violet_50,
            focusedLabelColor = Purple_500,
            cursorColor = Purple_500
        )
    )
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
private fun SearchPreview() {
    Infinite_TrackTheme {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .background(Purple_500)
        ) {
            InfiniteTrackSearchBar(
                value = "",
                placeholder = "Search for events...",
                onChange = {}
            )
        }
    }
}