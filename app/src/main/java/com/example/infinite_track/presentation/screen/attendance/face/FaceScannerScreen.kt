package com.example.infinite_track.presentation.screen.attendance.face

import android.graphics.Bitmap
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.infinite_track.presentation.components.cameras.FaceBoundingBox
import com.example.infinite_track.presentation.components.tittle.Tittle
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FaceScannerScreen(
    currentTime: String,
    currentAddress: String,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: FaceScannerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Camera executor
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

    // Handle navigation based on verification result
    LaunchedEffect(uiState.livenessState) {
        when (uiState.livenessState) {
            LivenessState.SUCCESS -> {
                // Set result and navigate back
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    "face_verification_result",
                    true
                )
                navController.popBackStack()
            }

            else -> {
                // Continue with current state
            }
        }
    }

    // Cleanup camera executor when composable is disposed
    DisposableEffect(Unit) {
        onDispose {
            cameraExecutor.shutdown()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        // Layer 1: Camera Preview with CameraX integration
        CameraPreview(
            modifier = Modifier.fillMaxSize(),
            cameraExecutor = cameraExecutor,
            onImageAnalysis = { imageProxy, bitmap ->
                viewModel.processImageProxy(imageProxy, bitmap)
            }
        )

        // Layer 2: Face Bounding Box Overlay
        FaceBoundingBox(
            modifier = Modifier.fillMaxSize(),
            boundingBox = uiState.boundingBox,
            livenessState = uiState.livenessState
        )

        // Layer 3: Top Info Section
        TopInfoSection(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 48.dp, start = 24.dp, end = 24.dp),
            currentTime = currentTime,
            currentAddress = currentAddress
        )

        // Layer 4: Instruction and Control Section
        InstructionSection(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp, start = 24.dp, end = 24.dp),
            uiState = uiState,
            onRetryClick = {
                viewModel.resetScanner()
            },
            onCloseClick = {
                navController.popBackStack()
            }
        )

        // Layer 5: Loading Overlay for processing state
        if (uiState.isProcessing) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.padding(32.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CircularProgressIndicator()
                        Text(
                            text = uiState.instructionText,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CameraPreview(
    modifier: Modifier = Modifier,
    cameraExecutor: ExecutorService,
    onImageAnalysis: (ImageProxy, Bitmap) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }

    AndroidView(
        factory = { previewView },
        modifier = modifier
    ) { view ->
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            // Preview use case
            val preview = Preview.Builder().build().also {
                it.surfaceProvider = view.surfaceProvider
            }

            // Image analysis use case
            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also { analysis ->
                    analysis.setAnalyzer(cameraExecutor) { imageProxy ->
                        // Convert ImageProxy to Bitmap for face detection
                        val bitmap = imageProxyToBitmap(imageProxy)
                        if (bitmap != null) {
                            onImageAnalysis(imageProxy, bitmap)
                        } else {
                            imageProxy.close()
                        }
                    }
                }

            // Select front camera
            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalysis
                )
            } catch (exc: Exception) {
                // Handle camera binding error
            }
        }, ContextCompat.getMainExecutor(context))
    }
}

@Composable
private fun TopInfoSection(
    modifier: Modifier = Modifier,
    currentTime: String,
    currentAddress: String
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Tittle(tittle = "Verifikasi Wajah")
            Text(
                text = currentTime,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = currentAddress,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun InstructionSection(
    modifier: Modifier = Modifier,
    uiState: FaceScannerState,
    onRetryClick: () -> Unit,
    onCloseClick: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Status Icon
            val icon = when (uiState.livenessState) {
                LivenessState.WAITING_FOR_LIVENESS -> {
                    if (uiState.currentChallenge == LivenessChallenge.BLINK) {
                        Icons.Default.Visibility
                    } else {
                        Icons.Default.SentimentSatisfied
                    }
                }

                LivenessState.SUCCESS -> Icons.Default.SentimentSatisfied
                else -> Icons.Default.Visibility
            }

            val iconColor = when (uiState.livenessState) {
                LivenessState.SUCCESS -> Color(0xFF00FFC2)
                LivenessState.FAILURE, LivenessState.TIMEOUT -> Color(0xFFFF3B30)
                LivenessState.WAITING_FOR_LIVENESS -> Color(0xFFFFD60A)
                else -> Color(0xFF00A3FF)
            }

            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = iconColor
            )

            // Instruction Text
            Text(
                text = uiState.instructionText,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )

            // Progress Indicator (for countdown)
            if (uiState.showCountdown && uiState.livenessState != LivenessState.SUCCESS) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    LinearProgressIndicator(
                        progress = uiState.progress,
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                    )
                    Text(
                        text = "${uiState.timeRemaining}s",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }

            // Error Message
            uiState.errorMessage?.let { errorMessage ->
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFFF3B30),
                    textAlign = TextAlign.Center
                )
            }

            // Action Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Close Button
                OutlinedButton(
                    onClick = onCloseClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Tutup")
                }

                // Retry Button (show when failed or timeout)
                if (uiState.livenessState == LivenessState.FAILURE ||
                    uiState.livenessState == LivenessState.TIMEOUT
                ) {
                    Button(
                        onClick = onRetryClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Coba Lagi")
                    }
                }
            }
        }
    }
}

// Helper function to convert ImageProxy to Bitmap
private fun imageProxyToBitmap(imageProxy: ImageProxy): Bitmap? {
    return try {
        val buffer = imageProxy.planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)

        // For now, create a placeholder bitmap
        // In production, you'd implement proper YUV to RGB conversion
        val width = imageProxy.width
        val height = imageProxy.height

        // Create a simple bitmap (placeholder implementation)
        createBitmap(width, height).apply {
            eraseColor(Color.Black.toArgb())
        }
    } catch (e: Exception) {
        null
    }
}
