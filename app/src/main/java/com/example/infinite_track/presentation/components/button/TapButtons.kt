package com.example.infinite_track.presentation.components.button

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.infinite_track.R
import com.example.infinite_track.presentation.theme.Blue_50
import com.example.infinite_track.presentation.theme.Blue_500

@Composable
fun TabButton(onTabSelected: (String) -> Unit) {
    val tabs = listOf("Work From Office", "Work From Home")
    var selectedTab by remember { mutableStateOf(tabs[0]) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.White,
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .background(color = Color(0x33FFFFFF)),
        horizontalArrangement = Arrangement.Absolute.Center
    ) {
        Button(
            onClick = {
                selectedTab = "Work From Office" // Setel tab yang dipilih ke WFO
                onTabSelected(selectedTab) // Panggil callback
            },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(if (selectedTab == "Work From Office") Blue_500 else Color(0x00FFFFFF))
        ) {
            Icon(
                painter = painterResource(id = R.drawable.wfo),
                contentDescription = "WFO Icon",
                tint = if (selectedTab == "Work From Office") Blue_50 else Blue_500,
//                modifier = Modifier.padding(horizontal = 4.dp)
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = stringResource(R.string.work_from_office),
                color = if (selectedTab == "Work From Office") Blue_50 else Blue_500
            )
        }

        // Tombol Work From Home
        Button(
//            contentPadding = PaddingValues(horizontal = 2.dp),
            onClick = {
                selectedTab = "Work From Home" // Setel tab yang dipilih ke WFH
                onTabSelected(selectedTab) // Panggil callback
            },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(if (selectedTab == "Work From Home") Blue_500 else Color(0x00FFFFFF))
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_wfh_icon),
                    contentDescription = "WFH Icon",
                    tint = if (selectedTab == "Work From Home") Blue_50 else Blue_500,
//                    modifier = Modifier.padding(horizontal = 4.dp)
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    text = "Work From Home",
                    color = if (selectedTab == "Work From Home") Blue_50 else Blue_500
                )
            }
        }
    }
}

@Preview
@Composable
fun TabButtonPreview() {
    TabButton(onTabSelected = {})
}

