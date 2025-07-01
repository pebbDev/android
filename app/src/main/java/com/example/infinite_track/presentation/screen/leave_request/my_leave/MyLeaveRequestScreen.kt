package com.example.infinite_track.presentation.screen.leave_request.my_leave

import FileMyLeaveCard
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.infinite_track.presentation.components.button.InfiniteTracButtonBack
import com.example.infinite_track.presentation.components.button.InfiniteTrackButton
import com.example.infinite_track.presentation.components.button.InfiniteTrackCancelButton
import com.example.infinite_track.presentation.components.cards.DataMyLeaveCard
import com.example.infinite_track.presentation.components.cards.UserMyLeaveCard
import com.example.infinite_track.presentation.components.progress.MyLeaveProgress
import com.example.infinite_track.presentation.theme.Infinite_TrackTheme

@Composable
fun MyLeaveRequestScreen() {
    val leaveData = listOf(
        "Jenis" to "Cuti Ikut Umroh",
        "Head Program" to "Reza Kurniawan",
        "Masa Cuti" to "17 Sept 2024 - 17 Oct 2024",
        "Total Cuti" to "30 Hari",
        "Deskripsi" to "Mau Umroh Lanjut Naik Haji",
        "Nomor Telepon" to "6285805304972",
        "Alamat" to "Medan, Sumatera Utara"
    )

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
//        BaseLayout()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 4.dp, end = 4.dp)
            ) {
                InfiniteTracButtonBack(
                    title = "Time Off - Requirement",
                    navigationBack = {}
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {

                val steps = listOf("Sign", "Pending", "Requested", "Approve")

                MyLeaveProgress(
                    steps = steps,
                    activeStep = 1
                )
                UserMyLeaveCard()
                Spacer(modifier = Modifier.height(12.dp))

                leaveData.forEach { (title, content) ->
                    DataMyLeaveCard(dataTitle = title, dataContent = content)
                    Spacer(modifier = Modifier.height(4.dp))
                }

                FileMyLeaveCard(dataTitle = "File", fileType = "pdf", filePath = "")

                Spacer(modifier = Modifier.height(12.dp))
                InfiniteTrackButton(onClick = { /*TODO*/ }, label = "Edit", isOutline = true)
                Spacer(modifier = Modifier.height(12.dp))
                InfiniteTrackCancelButton(onClick = { /*TODO*/ }, label = "Cancel")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MyLeavePreview() {
    Infinite_TrackTheme {
        MyLeaveRequestScreen()
    }
}