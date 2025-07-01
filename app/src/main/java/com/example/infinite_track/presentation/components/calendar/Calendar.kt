package com.example.infinite_track.presentation.components.calendar

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import com.example.infinite_track.R
import com.example.infinite_track.presentation.core.body1
import com.example.infinite_track.presentation.theme.Purple_300
import com.example.infinite_track.presentation.theme.Purple_400
import com.example.infinite_track.presentation.theme.Violet_50
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun DatePickerComponent(
    modifier: Modifier = Modifier,
    label: String,
    initialDate: String,
    onDateSelected: (String) -> Unit,
    enabled: Boolean = true,
    textColor: Color = Color.Gray
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    var selectedDate by remember { mutableStateOf(initialDate.ifEmpty { dateFormat.format(calendar.time) }) }

    val datePickerDialog = DatePickerDialog(
        context,
        R.style.CustomDatePickerDialogTheme,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            calendar.set(year, month, dayOfMonth)
            selectedDate = dateFormat.format(calendar.time)
            onDateSelected(selectedDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Violet_50.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(14.dp)
            .clickable(enabled = enabled) {
                if (enabled) {
                    datePickerDialog.show()
                }
            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = selectedDate.ifEmpty { label },
                    style = body1.copy(color = textColor)
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.ic_calendar),
                tint = Purple_300,
                contentDescription = null
            )
        }
    }
}

@Composable
fun DatePickerComponentWithLabel(
    modifier: Modifier = Modifier,
    label: String,
    initialDate: String,
    onDateSelected: (String) -> Unit,
    enabled: Boolean = true,
    textColor: Color = Color.Gray
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = label,
            style = body1.copy(color = Purple_400),
            modifier = Modifier.padding(bottom = 4.dp)
        )

        DatePickerComponent(
            modifier = Modifier.fillMaxWidth(),
            label = label,
            initialDate = initialDate,
            onDateSelected = onDateSelected,
            enabled = enabled,
            textColor = textColor // Pass the textColor to DatePickerComponent
        )
    }
}


@Composable
fun ContractDatePicker() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        DatePickerComponentWithLabel(
            modifier = Modifier.weight(1f),
            label = "Start Contract",
            initialDate = "09/09/2024",
            onDateSelected = { startDate -> },
            enabled = true
        )

        Spacer(modifier = Modifier.width(16.dp))

        DatePickerComponentWithLabel(
            modifier = Modifier.weight(1f),
            label = "End Contract",
            initialDate = "09/03/2024",
            onDateSelected = { endDate -> },
            enabled = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ContractDatePickerPreview() {
    ContractDatePicker()
}
