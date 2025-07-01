package com.example.infinite_track.presentation.components.cameras


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.io.File

@Composable
fun CameraCapture(
    modifier: Modifier = Modifier,
    onImageCaptured: (Uri?) -> Unit,
    onError: (ImageCaptureException) -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")

    val size by infiniteTransition.animateValue(
        initialValue = 80.dp,
        targetValue = 64.dp,
        Dp.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )
    val smallCircle by infiniteTransition.animateValue(
        initialValue = 72.dp,
        targetValue = 64.dp,
        Dp.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }
    var lensFacing by remember { mutableStateOf(CameraSelector.LENS_FACING_FRONT) }
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val previewView = remember { PreviewView(context) }
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

    LaunchedEffect(lensFacing) {
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner, cameraSelector, preview, imageCapture
                )
            } catch (exc: Exception) {
                onError(exc as ImageCaptureException)
            }
        }, ContextCompat.getMainExecutor(context))
    }

    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        capturedBitmap?.let { bitmap ->

            Box(
                modifier = Modifier
                    .fillMaxSize(),
            ){

                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Captured Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center),
                    contentScale = ContentScale.Crop
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .padding(bottom = 20.dp)
                        .align(Alignment.BottomCenter)
                    ,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = {
                        onImageCaptured(capturedImageUri)
                    },
                        colors = ButtonDefaults.buttonColors(Color(0X80FFFFFF)),
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(60.dp),
                        shape = CircleShape,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Check,
                            contentDescription = "Accept Icon",
                            tint = Color.White,
                            modifier = Modifier.size(50.dp)
                        )
                    }

                    Button(onClick = {
                        capturedBitmap = null
                    },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0X80FFFFFF),
                        ),
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(60.dp),
                        shape = CircleShape,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Cancel,
                            contentDescription = "Accept Icon",
                            tint = Color.White,
                            modifier = Modifier.size(50.dp)
                            )

                    }
                }
            }

        } ?: run {
            Column(modifier = modifier) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                    ,
                ){
                    AndroidView(
                        { previewView },
                        modifier = Modifier.fillMaxSize()
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
//                            .padding(bottom = 16.dp)
                    ) {

                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                            ,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ){

                            Spacer(modifier = Modifier.weight(1.5f))

                            IconButton(
                                modifier = Modifier
                                    .size(84.dp),
                                onClick = {
                                    val outputFile = createFile(context)
                                    val outputOptions = ImageCapture.OutputFileOptions.Builder(outputFile).build()
                                    imageCapture?.takePicture(
                                        outputOptions,
                                        ContextCompat.getMainExecutor(context),
                                        object : ImageCapture.OnImageSavedCallback {
                                            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                                capturedImageUri = outputFileResults.savedUri
                                                capturedBitmap = BitmapFactory.decodeFile(outputFile.absolutePath)

                                                // Menggunakan ExifInterface untuk mendapatkan orientasi
                                                val exif = ExifInterface(outputFile.absolutePath)
                                                val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

                                                capturedBitmap = rotateBitmap(capturedBitmap!!, orientation)

                                                if (lensFacing == CameraSelector.LENS_FACING_FRONT){
                                                    capturedBitmap = capturedBitmap?.mirrorImage()
                                                }
                                            }

                                            override fun onError(exception: ImageCaptureException) {
                                                onError(exception)
                                            }
                                        }
                                    )
                                }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(size)
                                        .clip(CircleShape)
                                        .background(Color(0xFFC9A6FF).copy(alpha = 0.50f))
                                )
                                Box(
                                    modifier = Modifier
                                        .size(smallCircle)
                                        .clip(CircleShape)
                                        .background(Color(0xFFB17DFF).copy(alpha = 0.50f))
                                )
                            }

                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(
                                onClick = {
                                    lensFacing = if (lensFacing == CameraSelector.LENS_FACING_FRONT) {
                                        CameraSelector.LENS_FACING_BACK
                                    } else {
                                        CameraSelector.LENS_FACING_FRONT
                                    }
                                },
                                modifier = Modifier
                                    .size(60.dp)
                                    .padding(start = 16.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FlipCameraAndroid,
                                    contentDescription = "Flip Camera",
                                    tint = Color.White,
                                    modifier = Modifier.size(40.dp)
                                )
                            }


                        }
                    }


                }
            }
        }
    }
}

private fun createFile(context: Context): File {
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile("photo_", ".jpg", storageDir)
}

private fun rotateBitmap(bitmap: Bitmap, orientation: Int): Bitmap {
    val matrix = Matrix()
    when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
        ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
        ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
        else -> return bitmap
    }
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}

fun Bitmap.mirrorImage():Bitmap {
    val matrix = Matrix().apply {
        preScale(-1f, 1f)
    }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, false)
}

