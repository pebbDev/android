package com.example.infinite_track.presentation.components.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.infinite_track.R
import com.example.infinite_track.presentation.core.body1
import com.example.infinite_track.presentation.theme.Purple_300
import com.example.infinite_track.presentation.theme.Purple_500
import com.example.infinite_track.presentation.theme.Violet_50

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneNumberInputText(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String = "",
    onChange: (String) -> Unit = {},
    leadingIcon: Painter,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(Violet_50.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(15.dp),
        leadingIcon = {
            Icon(
                painter = leadingIcon,
                tint = Purple_500,
                contentDescription = ""
            )
        },
        placeholder = {
            Text(
                placeholder,
                style = body1,
                color = Purple_300
            )
        },
        visualTransformation = VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
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
fun PhoneNumberInputTextPreview() {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .background(Purple_500)
    ) {
        PhoneNumberInputText(
            value = "",
            leadingIcon = painterResource(id = R.drawable.ic_phone),
            placeholder = "Enter phone number",
            onChange = {
            }
        )
    }
}