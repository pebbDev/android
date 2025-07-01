package com.example.infinite_track.presentation.components.profile_textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.infinite_track.presentation.theme.Purple_400
import com.example.infinite_track.presentation.theme.Violet_50

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneNumberTextFieldComponent(
    modifier: Modifier = Modifier,
    label: String = "Phone Number",
    value: String = "",
    enabled: Boolean = true,
    onValueChange: (String) -> Unit = {}
) {
    var textState by rememberSaveable { mutableStateOf(value) }

    if (textState != value) {
        textState = value
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = label,
            color = Purple_400,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        TextField(
            value = textState,
            onValueChange = {
                textState = it
                onValueChange(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(
                    color = Violet_50.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(15.dp)
                ),
            shape = RoundedCornerShape(15.dp),
            enabled = enabled,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                cursorColor = Color.Black
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone
            ),
            visualTransformation = VisualTransformation.None
        )
    }
}

@Preview
@Composable
fun PhoneNumberTextFieldComponentPreview() {
    PhoneNumberTextFieldComponent(
        label = "Phone Number"
    )
}