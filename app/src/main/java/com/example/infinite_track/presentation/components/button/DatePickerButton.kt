package com.example.infinite_track.presentation.components.button

import android.app.DatePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.infinite_track.R
import com.example.infinite_track.presentation.core.body1
import com.example.infinite_track.presentation.theme.Blue_500
import com.example.infinite_track.presentation.theme.Infinite_TrackTheme
import com.example.infinite_track.presentation.theme.White
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun DatePickerButton(
    onDateSelected: (String) -> Unit
) {
    // Get current date
    val calendar = Calendar.getInstance()
    val currentYear = calendar.get(Calendar.YEAR)
    val currentMonth = calendar.get(Calendar.MONTH)
    val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

    // Save the current date for comparison
    val todayDate = calendar.time

    // SimpleDateFormat untuk menampilkan nama bulan
    val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

    // State untuk menyimpan tanggal yang dipilih
    var selectedDate by remember {
        mutableStateOf(dateFormat.format(calendar.time))
    }

    // Function untuk membuka DatePickerDialog
    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        R.style.CustomDatePickerDialogTheme,
        { _, year, month, dayOfMonth ->
            // Update state dengan tanggal yang dipilih
            calendar.set(year, month, dayOfMonth)
            val newDate = dateFormat.format(calendar.time)
            selectedDate = newDate
            onDateSelected(newDate)
        },
        currentYear,
        currentMonth,
        currentDay
    )

    // Button untuk menampilkan tanggal dan membuka DatePicker saat ditekan
    Button(
        onClick = {
            datePickerDialog.show()
        },
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(width = 1.dp, color = White),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0x30FFFFFF)),
        modifier = Modifier
            .width(430.dp)
            .height(38.dp)
    ) {
        // Compare selected date with today's date
        calendar.set(currentYear, currentMonth, currentDay)  // Set calendar to current date for comparison
        val isToday = dateFormat.format(todayDate) == selectedDate
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.CenterStart
        ){
            Text(
                text = if (isToday) {
                    "Today, $selectedDate"
                } else {
                    selectedDate
                },
                style = body1,
                color = Blue_500
            )
            Image(
                painter = painterResource(id = R.drawable.ic_calender),
                contentDescription = "Icon Calendar",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewDatePickerButton() {
    Infinite_TrackTheme {
        DatePickerButton(
            onDateSelected = {}
        )
    }
}