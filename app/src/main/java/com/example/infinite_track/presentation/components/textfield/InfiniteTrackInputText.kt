package com.example.infinite_track.presentation.components.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.infinite_track.R
import com.example.infinite_track.presentation.core.body1
import com.example.infinite_track.presentation.theme.Blue_500
import com.example.infinite_track.presentation.theme.Purple_300
import com.example.infinite_track.presentation.theme.Purple_500
import com.example.infinite_track.presentation.theme.Violet_50

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThriveInInputText(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String = "",
    onChange: (String) -> Unit = {},
    isObsecure: Boolean? = null,
    isNotWide: Boolean = false,
    leadingIcon: Painter,
    keyboardType: KeyboardType = KeyboardType.Text,
) {


    var isShowPassword by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        modifier = if (isNotWide) modifier
            .clip(RoundedCornerShape(15.dp))
            .background(Violet_50.copy(alpha = 0.5f)) else modifier
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
        trailingIcon = {
            if (isObsecure != null && isObsecure) {
                IconButton(onClick = {
                    isShowPassword = !isShowPassword
                }) {
                    Icon(
                        painter = painterResource(id = if (!isShowPassword) R.drawable.visible else R.drawable.visible_off),
                        contentDescription = "Toggle Password Visibility"
                    )
                }
            }

        },
        visualTransformation = if (isObsecure != null && isObsecure) {
            if (isShowPassword) VisualTransformation.None else PasswordVisualTransformation()
        } else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Blue_500,
            unfocusedBorderColor = Violet_50,
            focusedLabelColor = Purple_500,
            cursorColor = Purple_500
        )
    )

}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun ThriveInInputTextPreview() {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .background(Purple_500)
    ) {
        ThriveInInputText(
            value = "",
            leadingIcon = painterResource(id = R.drawable.ic_headprogram),
            placeholder = "Jangan bandel",
            onChange = {

            },
            isObsecure = true
        )
    }
}