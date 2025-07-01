package com.example.infinite_track.presentation.components.calendar

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.example.infinite_track.R
import com.example.infinite_track.presentation.theme.Purple_400
import com.example.infinite_track.presentation.theme.Purple_500
import com.example.infinite_track.presentation.theme.Violet_50
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun Date(
    modifier: Modifier = Modifier,
    label: String,
    initialDate: String,
    onDateSelected: (String) -> Unit,
    leadingIcon: Painter,
    enabled: Boolean = true
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    var selectedDate by remember { mutableStateOf(initialDate.ifEmpty { label }) }

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
            .background(color = Violet_50.copy(alpha = 0.5f), shape = RoundedCornerShape(12.dp))
            .border(width = 1.dp, color = Violet_50, shape = RoundedCornerShape(12.dp))
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
                Icon(
                    painter = leadingIcon,
                    tint = Purple_500,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = selectedDate,
                    color = Purple_400
                )
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun DatePreview() {
    Date(
        label = "Start Date",
        initialDate = "",
        onDateSelected = {},
        leadingIcon = painterResource(id = R.drawable.ic_calendar)
    )
}
