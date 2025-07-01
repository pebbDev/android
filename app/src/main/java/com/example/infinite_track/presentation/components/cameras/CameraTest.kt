package com.example.infinite_track.presentation.components.cameras

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.infinite_track.R
import com.example.infinite_track.utils.showToast

@Composable
fun CameraTestScreen() {
    val context = LocalContext.current

    var notes by remember { mutableStateOf("") }

    // Camera
    var showCamera by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                showCamera = true
            } else {
                showToast(context, R.string.camera_permission_denied)
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            imageUri?.let { uri ->
                Image(
                    painter = rememberImagePainter(data = uri),
                    contentDescription = "Captured Image",
                    modifier = Modifier
                        .size(200.dp)
                        .weight(1f)
                )
            }
            Button(
                onClick = {
                    launcher.launch(android.Manifest.permission.CAMERA)
                },
                colors = ButtonDefaults.buttonColors(Color.Transparent),
                elevation = null,
                modifier = Modifier
                    .border(
                        BorderStroke(2.dp, Color.White),
                        RoundedCornerShape(10.dp)
                    )
                    .background(Color(0x80F3ECFF), RoundedCornerShape(10.dp))
                    .size(100.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_cameras),
                    contentDescription = "Camera Icon",
                    modifier = Modifier.size(50.dp),
                    contentScale = ContentScale.Fit,
                )
            }
        }
        if (showCamera) {
            CameraCapture(onImageCaptured = { uri ->
                if (uri != null) {
                    imageUri = uri
                }
                showCamera = false
            }) { }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun CameraTestPreview() {
    CameraTestScreen()
}