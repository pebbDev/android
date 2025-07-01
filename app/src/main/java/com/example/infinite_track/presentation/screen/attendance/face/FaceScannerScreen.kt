package com.example.infinite_track.presentation.screen.attendance.face

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.infinite_track.presentation.components.tittle.Tittle
import com.example.infinite_track.presentation.theme.Infinite_TrackTheme
import com.example.infinite_track.utils.CameraControls

@Composable
fun FaceScannerScreen(
    onCloseClick: () -> Unit,
    onCaptureClick: () -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // State lokal untuk mengontrol UI antara mode pemindaian dan mode konfirmasi.
    var isImageCaptured by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        // Lapis 1: Camera Preview (disimulasikan dengan Box gelap)
        // Di aplikasi nyata, ganti Composable ini dengan komponen CameraX Anda.
        CameraPreviewPlaceholder()

        // Lapis 2: Informasi di bagian atas (dengan latar transparan)
        TopInfoSection(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 48.dp, start = 24.dp, end = 24.dp),
            currentTime = "10:30 AM", // Data statis untuk preview
            currentAddress = "Jl. Jenderal Sudirman No.Kav. 52-53, Jakarta Selatan" // Data statis
        )

        // Lapis 3: Tombol Kontrol di bagian bawah
        CameraControls(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp),
            isImageCaptured = isImageCaptured,
            onCloseClick = {
                if (isImageCaptured) {
                    isImageCaptured = false // Kembali ke mode pemindaian (Ambil Ulang)
                } else {
                    onCloseClick() // Tutup layar
                }
            },
            onCaptureClick = {
                onCaptureClick()
                isImageCaptured = true // Pindah ke mode konfirmasi
            },
            onConfirmClick = {
                onConfirmClick()
            }
        )
    }
}

@Composable
private fun CameraPreviewPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        // Placeholder ini akan ditimpa oleh CameraX di implementasi nyata.
        Text("Camera Preview Area", color = Color.White.copy(alpha = 0.5f))
    }
}

@Composable
private fun TopInfoSection(
    modifier: Modifier = Modifier,
    currentTime: String,
    currentAddress: String
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Tittle(tittle = "Verifikasi Wajah")
        Text(
            text = currentTime,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = currentAddress,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun FaceScannerScreenPreview() {
    Infinite_TrackTheme {
        FaceScannerScreen(
            onCloseClick = {},
            onCaptureClick = {},
            onConfirmClick = {}
        )
    }
}
