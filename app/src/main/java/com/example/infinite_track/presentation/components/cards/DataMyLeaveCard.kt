package com.example.infinite_track.presentation.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.CardDefaults.outlinedCardBorder
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.infinite_track.presentation.core.body1
import com.example.infinite_track.presentation.theme.Infinite_TrackTheme

@Composable
fun DataMyLeaveCard(
    dataTitle: String,
    dataContent: String,
) {
    Card(
        border = outlinedCardBorder(),
        colors = cardColors(Color(0x33FFFFFF)),
        modifier = Modifier
            .width(433.dp)
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally // Memastikan konten mulai dari kiri
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = dataTitle,
                    style = body1,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.weight(1f) // Berat 1 untuk pembagian yang merata
                )

                Text(
                    text = dataContent,
                    style = body1.copy(lineHeight = 16.sp),
                    fontSize = 12.sp,
                    modifier = Modifier.weight(1f), // Berat 1 untuk pembagian yang merata
                    maxLines = 2,
                    overflow = TextOverflow.Clip
                )
            }
        }
    }
}

@Preview
@Composable
fun DataMyLeaveCardPreview() {
    Infinite_TrackTheme {
        DataMyLeaveCard(
            dataTitle = "Jenis Cuti",
            dataContent = "Cuti Healing Bos Yahaha hayyuuuuu. Maju lo sini dekkkk"
        )
    }
}
