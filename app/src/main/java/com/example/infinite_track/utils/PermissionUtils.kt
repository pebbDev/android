package com.example.infinite_track.utils

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Utility object for permission-related UI components
 */
object PermissionUtils {

    /**
     * A dialog that explains why a permission is needed and asks the user to go to settings.
     */
    @Composable
    fun PermissionRationaleDialog(
        onConfirm: () -> Unit,
        onDismiss: () -> Unit
    ) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = "Izin Diperlukan",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = "Untuk fungsionalitas penuh, aplikasi ini memerlukan izin lokasi latar belakang. " +
                           "Tanpa itu, fitur geofencing tidak akan berfungsi saat aplikasi ditutup. " +
                           "Silakan aktifkan izin dari halaman pengaturan aplikasi.",
                    fontSize = 16.sp
                )
            },
            confirmButton = {
                Button(onClick = onConfirm) {
                    Text("Buka Pengaturan")
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text("Tolak")
                }
            }
        )
    }
}
