package com.example.infinite_track.presentation.components.address

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.infinite_track.presentation.theme.Purple_400
import com.example.infinite_track.presentation.theme.Violet_50


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextArea(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    value: String = "",
    onTextChanged: (String) -> Unit = {},
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "Address",
            color = Purple_400,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        TextField(
            value = value,
            onValueChange = { onTextChanged(it) },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(
                    color = Violet_50.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(15.dp)
                ),
            shape = RoundedCornerShape(12.dp),
            enabled = enabled,
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

@Preview
@Composable
fun CustomTextAreaPreview() {
    CustomTextArea(
        enabled = true
    )
}
