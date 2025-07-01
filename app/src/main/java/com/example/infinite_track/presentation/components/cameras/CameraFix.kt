package com.example.infinite_track.presentation.components.cameras

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.infinite_track.R
import com.example.infinite_track.presentation.core.body1
import com.example.infinite_track.presentation.core.headline3
import com.example.infinite_track.presentation.theme.Infinite_TrackTheme
import com.example.infinite_track.presentation.theme.Purple_300
import com.example.infinite_track.utils.showToast

@Composable
fun CameraC (
    modifier: Modifier = Modifier,
    timetAtt: String,
    att: String,
    cardImage: Int,
    isWFH: Boolean
) {

    var showCamera by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }


    var isImageUploaded by remember { mutableStateOf(false) }

    val context = LocalContext.current
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

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .background(color = Color(0x33FFFFFF), shape = RoundedCornerShape(10.dp))
                    .border(width = 1.dp, color = Color.White, shape = RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
            }

            Spacer(modifier = Modifier.padding(14.dp))

                Spacer(modifier = Modifier.padding(10.dp))

                // Camera Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                ) {
                    // Camera1
                    if (!isImageUploaded) {
                        Button(
                            onClick = {
                                launcher.launch(android.Manifest.permission.CAMERA)
                            },
                            colors = ButtonDefaults.buttonColors(Color.Transparent),
                            elevation = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    BorderStroke(2.dp, Color.White),
                                    RoundedCornerShape(10.dp)
                                )
                                .background(Color(0x80F3ECFF), RoundedCornerShape(10.dp))
                                .size(200.dp),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally

                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.cameras2),
                                    contentDescription = "Camera Icon",
                                    modifier = Modifier.size(50.dp),
                                    contentScale = ContentScale.Fit,
                                )
                                Spacer(modifier = Modifier.padding(7.dp))

                                Text(
                                    text = "A Photo of You",
                                    style = headline3,
                                )

                                Spacer(modifier = Modifier.padding(5.dp))

                                Text(
                                    text = "Please make a sure your photo clearly show your face",
                                    style = body1,
                                    color = Purple_300
                                )
                            }
                        }
                    }
                    imageUri?.let { uri ->
                        Spacer(modifier = Modifier.height(16.dp))
                        Image(
                            painter = rememberImagePainter(data = uri),
                            contentDescription = "Captured Image",
                            modifier = Modifier.size(200.dp)
                        )
                    }

                    Spacer(modifier = Modifier.padding(16.dp))
                }
            }
        }

@Preview(showBackground = true)
@Composable
fun PreviewCameraScreen() {
    Infinite_TrackTheme {
        CameraC(
            timetAtt = "10:24 AM",
            att = "Check In",
            cardImage = R.drawable.ic_checkin,
//            label = "Send",
            isWFH = true
        )
    }
}
