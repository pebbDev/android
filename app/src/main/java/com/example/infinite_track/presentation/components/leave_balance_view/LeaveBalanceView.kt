package com.example.infinite_track.presentation.components.leave_balance_view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LeaveBalanceView(leaveBalance: Int) {
    val textColor = if (leaveBalance == 0) Color.Red else Color(0xFF9B5CE6)

    Text(
        text = "Current Leave Balance $leaveBalance Days",
        fontSize = 12.sp,
        color = textColor,
        fontWeight = FontWeight.Bold
    )
}

@Preview
@Composable
fun LeaveBalancePreview() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        LeaveBalanceView(leaveBalance = 12)

        Spacer(modifier = Modifier.height(16.dp))

        LeaveBalanceView(leaveBalance = 0)
    }
}
