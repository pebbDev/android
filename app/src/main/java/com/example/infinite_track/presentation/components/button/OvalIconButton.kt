package com.example.infinite_track.presentation.components.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.infinite_track.R
import com.example.infinite_track.presentation.theme.Blue_500
import com.example.infinite_track.presentation.theme.Infinite_TrackTheme

/**
 * Komponen IconButton dengan latar belakang oval semi-transparan.
 *
 * @param onClick Aksi yang dijalankan saat tombol diklik.
 * @param icon Painter resource untuk ikon yang akan ditampilkan.
 * @param contentDescription Deskripsi untuk aksesibilitas.
 * @param modifier Modifier untuk kustomisasi dari luar.
 * @param tint Warna dari ikon. Defaultnya adalah warna primary dari tema.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OvalIconButton(
    onClick: () -> Unit,
    icon: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = Blue_500

) {
    Surface(
        onClick = onClick,
        modifier = modifier.size(48.dp), // Making it square for a perfect circle
        shape = CircleShape,
        color = Color.White.copy(alpha = 0.5f),
        contentColor = tint, // Color for the icon
            border = BorderStroke(1.dp, Color.White)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = contentDescription,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF000000) // Latar belakang gelap agar transparan terlihat
@Composable
fun OvalIconButtonPreview() {
    Infinite_TrackTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            OvalIconButton(
                onClick = { /* Aksi klik di sini */ },
                // Menggunakan ikon yang ada di proyek
                icon = painterResource(id = R.drawable.ic_searching),
                contentDescription = "Search Button"
            )
        }
    }
}
