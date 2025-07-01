package com.example.infinite_track.presentation.components.cameras

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.infinite_track.R


@Composable
fun CameraCapturePreview(
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = { /* Do nothing in preview */ },
            modifier
                .size(100.dp)
                .background(Color.White.copy(alpha = 0.8f), CircleShape)
        ) {
            Icon(
                painter = rememberImagePainter(data = R.drawable.ic_cameras),
                contentDescription = "Add Image",
                tint = Color.Gray
            )
        }
    }
}

@Composable
fun UploadImageScreens(
    modifier: Modifier = Modifier
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val isPreview = LocalInspectionMode.current

    Column(
        modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.add_image),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (isPreview) {
            CameraCapturePreview()
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                imageUri?.let { uri ->
                    Image(
                        painter = rememberImagePainter(data = uri),
                        contentDescription = "Captured Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } ?: run {
                    IconButton(
                        onClick = { /* Open camera to capture image */ },
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color.White.copy(alpha = 0.8f), CircleShape)
                    ) {
                        Icon(
                            painter = rememberImagePainter(data = R.drawable.ic_cameras),
                            contentDescription = "Add Image",
                            tint = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            CameraCapture(
                onImageCaptured = { uri -> imageUri = uri },
                onError = { exception -> exception.printStackTrace() }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

    }
}

@Preview(showBackground = true)
@Composable
fun UploadImageScreenPreview() {
    UploadImageScreens()
}