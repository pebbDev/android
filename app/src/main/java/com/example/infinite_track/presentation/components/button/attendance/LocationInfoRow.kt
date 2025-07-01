package com.example.infinite_track.presentation.components.button.attendance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.infinite_track.presentation.core.body1
import com.example.infinite_track.presentation.core.headline3
import com.example.infinite_track.presentation.core.headline4
import com.example.infinite_track.presentation.theme.Infinite_TrackTheme

/**
 * Komponen untuk menampilkan informasi lokasi dengan ikon, judul, dan alamat.
 *
 * @param icon Ikon yang akan ditampilkan di sebelah kiri
 * @param title Judul lokasi
 * @param address Alamat lengkap lokasi
 */
@Composable
fun LocationInfoRow(
    icon: ImageVector,
    title: String,
    address: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier.size(24.dp)
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = title,
                style = headline3
            )

            Text(
                text = address,
                style = headline4,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LocationInfoRowPreview() {
    Infinite_TrackTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LocationInfoRow(
                icon = Icons.Default.LocationOn,
                title = "Target Location",
                address = "Jl. Sudirman No. 123, Jakarta Pusat, DKI Jakarta"
            )

            LocationInfoRow(
                icon = Icons.Default.LocationOn,
                title = "Current Location",
                address = "Jl. Thamrin No. 456, Jakarta Pusat, DKI Jakarta"
            )
        }
    }
}
