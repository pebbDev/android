package com.example.infinite_track.presentation.components.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.infinite_track.presentation.core.body1
import com.example.infinite_track.presentation.theme.Infinite_TrackTheme
import com.example.infinite_track.presentation.theme.Purple_300
import com.example.infinite_track.presentation.theme.Purple_500
import com.example.infinite_track.presentation.theme.Violet_50

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumberTextFieldComponent(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    enabled: Boolean = true
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                // Only allow digits and filter out non-numeric characters
                val filteredValue = newValue.filter { char -> char.isDigit() }
                onValueChange(filteredValue)
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(15.dp))
                .background(Violet_50.copy(alpha = 0.5f)),
            shape = RoundedCornerShape(15.dp),
            placeholder = {
                if (placeholder.isNotEmpty()) {
                    Text(
                        placeholder,
                        style = body1,
                        color = Purple_300
                    )
                }
            },
            enabled = enabled,
            visualTransformation = VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
}

@Preview(showBackground = true)
@Composable
fun NumberTextFieldComponentPreview() {
    Infinite_TrackTheme {
        NumberTextFieldComponent(
            label = "Radius (meters)",
            value = "1000",
            onValueChange = {},
            placeholder = "Enter radius in meters"
        )
    }
}
