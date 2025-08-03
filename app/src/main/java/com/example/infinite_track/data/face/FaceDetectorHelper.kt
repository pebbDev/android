package com.example.infinite_track.data.face

import android.graphics.Bitmap
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Enum for liveness detection results
 * Provides progressive feedback for user guidance
 */
enum class LivenessResult {
    SUCCESS,    // Liveness detected successfully
    IN_PROGRESS, // User is on the right track, needs slight adjustment
    FAILURE     // Liveness not detected
}

/**
 * Helper class for ML Kit Face Detection operations
 * Handles face detection, liveness verification (blink/smile), and face extraction
 */
@Singleton
class FaceDetectorHelper @Inject constructor() {

    companion object {
        // More flexible thresholds for progressive feedback
        private const val BLINK_HIGH_THRESHOLD = 0.4f // Original threshold for SUCCESS
        private const val BLINK_LOW_THRESHOLD = 0.6f  // Lower threshold for IN_PROGRESS

        private const val SMILE_HIGH_THRESHOLD = 0.7f // Original threshold for SUCCESS
        private const val SMILE_MEDIUM_THRESHOLD = 0.4f // Medium threshold for IN_PROGRESS
        private const val SMILE_LOW_THRESHOLD = 0.2f   // Minimum threshold for any detection
    }

    // ML Kit Face Detector with optimized settings
    private val faceDetector: FaceDetector by lazy {
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setMinFaceSize(0.15f) // Minimum face size relative to image
            .enableTracking() // Enable face tracking for better performance
            .build()

        FaceDetection.getClient(options)
    }

    /**
     * Detects faces in the given image frame
     * @param imageProxy Camera image frame from CameraX
     * @param onResult Callback with detection result
     */
    @OptIn(ExperimentalGetImage::class)
    fun detect(imageProxy: ImageProxy, onResult: (Result<Face>) -> Unit) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            faceDetector.process(image)
                .addOnSuccessListener { faces ->
                    println("ML Kit face detection completed. Found ${faces.size} faces")
                    if (faces.isNotEmpty()) {
                        // Return the first (largest) detected face
                        val largestFace =
                            faces.maxByOrNull { it.boundingBox.width() * it.boundingBox.height() }
                        if (largestFace != null) {
                            println("Largest face found at: ${largestFace.boundingBox}")
                            onResult(Result.success(largestFace))
                        } else {
                            println("No valid face detected despite faces list not empty")
                            onResult(Result.failure(Exception("No valid face detected")))
                        }
                    } else {
                        println("No faces detected in current frame")
                        onResult(Result.failure(Exception("No faces detected")))
                    }
                }
                .addOnFailureListener { exception ->
                    println("ML Kit face detection failed: ${exception.message}")
                    onResult(Result.failure(exception))
                }
                .addOnCompleteListener {
                    // Clean up resources - ALWAYS close imageProxy here
                    imageProxy.close()
                    println("ImageProxy closed after ML Kit processing")
                }
        } else {
            println("MediaImage is null in imageProxy")
            onResult(Result.failure(Exception("Image is null")))
            imageProxy.close()
        }
    }

    /**
     * Verifies if the person is blinking (liveness detection) with progressive feedback
     * @param face Detected face from ML Kit
     * @return LivenessResult indicating blink detection status
     */
    fun verifyBlink(face: Face): LivenessResult {
        val leftEyeOpenProbability = face.leftEyeOpenProbability
        val rightEyeOpenProbability = face.rightEyeOpenProbability

        return if (leftEyeOpenProbability != null && rightEyeOpenProbability != null) {
            val avgEyeOpenProbability = (leftEyeOpenProbability + rightEyeOpenProbability) / 2f

            println("DEBUG: Blink detection - Left eye: $leftEyeOpenProbability, Right eye: $rightEyeOpenProbability, Average: $avgEyeOpenProbability")

            when {
                // SUCCESS: Both eyes clearly closed (blinking)
                leftEyeOpenProbability < BLINK_HIGH_THRESHOLD && rightEyeOpenProbability < BLINK_HIGH_THRESHOLD -> {
                    println("DEBUG: Blink SUCCESS - Both eyes closed")
                    LivenessResult.SUCCESS
                }
                // IN_PROGRESS: One eye closed or both eyes partially closed
                avgEyeOpenProbability < BLINK_LOW_THRESHOLD -> {
                    println("DEBUG: Blink IN_PROGRESS - Getting closer to blinking")
                    LivenessResult.IN_PROGRESS
                }
                // FAILURE: Eyes too open
                else -> {
                    println("DEBUG: Blink FAILURE - Eyes still open")
                    LivenessResult.FAILURE
                }
            }
        } else {
            println("DEBUG: Blink FAILURE - Eye probabilities not available")
            LivenessResult.FAILURE // Cannot determine blink if probabilities are not available
        }
    }

    /**
     * Verifies if the person is smiling (liveness detection) with progressive feedback
     * @param face Detected face from ML Kit
     * @return LivenessResult indicating smile detection status
     */
    fun verifySmile(face: Face): LivenessResult {
        val smilingProbability = face.smilingProbability

        return if (smilingProbability != null) {
            println("DEBUG: Smile detection - Probability: $smilingProbability")

            when {
                // SUCCESS: Strong smile detected
                smilingProbability > SMILE_HIGH_THRESHOLD -> {
                    println("DEBUG: Smile SUCCESS - Strong smile detected")
                    LivenessResult.SUCCESS
                }
                // IN_PROGRESS: Moderate smile, encourage user to smile more
                smilingProbability > SMILE_MEDIUM_THRESHOLD -> {
                    println("DEBUG: Smile IN_PROGRESS - Moderate smile, needs more")
                    LivenessResult.IN_PROGRESS
                }
                // FAILURE: Little to no smile
                else -> {
                    println("DEBUG: Smile FAILURE - Not smiling enough")
                    LivenessResult.FAILURE
                }
            }
        } else {
            println("DEBUG: Smile FAILURE - Smile probability not available")
            LivenessResult.FAILURE // Cannot determine smile if probability is not available
        }
    }

    /**
     * Extracts and preprocesses face bitmap for consistent embedding generation
     * @param face Detected face from ML Kit
     * @param image Source bitmap from camera
     * @return Preprocessed face bitmap or null if extraction fails
     */
    fun extractFaceBitmap(face: Face, image: Bitmap): Bitmap? {
        return try {
            val boundingBox = face.boundingBox

            // DEBUG: Log original face detection info
            println("DEBUG FaceDetectorHelper: Original bounding box: $boundingBox")
            println("DEBUG FaceDetectorHelper: Source image size: ${image.width}x${image.height}")

            // Add padding around the face for better context (10% on each side)
            val padding = (boundingBox.width() * 0.1f).toInt()

            // Calculate expanded bounding box with padding
            val left = maxOf(0, boundingBox.left - padding)
            val top = maxOf(0, boundingBox.top - padding)
            val right = minOf(image.width, boundingBox.right + padding)
            val bottom = minOf(image.height, boundingBox.bottom + padding)

            println("DEBUG FaceDetectorHelper: Padded coordinates - left:$left, top:$top, right:$right, bottom:$bottom")
            println("DEBUG FaceDetectorHelper: Crop dimensions: ${right - left}x${bottom - top}")

            // Validate that we have a valid crop area
            if (left < right && top < bottom) {
                // Extract face with padding
                val croppedBitmap =
                    Bitmap.createBitmap(image, left, top, right - left, bottom - top)
                println("DEBUG FaceDetectorHelper: Cropped bitmap size: ${croppedBitmap.width}x${croppedBitmap.height}")

                // KUNCI PERBAIKAN: Standardisasi ukuran dan format
                val standardizedBitmap = standardizeFaceBitmap(croppedBitmap)
                println("DEBUG FaceDetectorHelper: Standardized bitmap size: ${standardizedBitmap.width}x${standardizedBitmap.height}")

                // Clean up intermediate bitmap
                if (croppedBitmap != standardizedBitmap) {
                    croppedBitmap.recycle()
                }

                standardizedBitmap
            } else {
                println("DEBUG FaceDetectorHelper: Invalid crop area - left:$left >= right:$right or top:$top >= bottom:$bottom")
                null
            }
        } catch (e: Exception) {
            println("DEBUG FaceDetectorHelper: Error extracting face bitmap: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    /**
     * Standardizes face bitmap to consistent size and format
     * This ensures consistent preprocessing for both profile and camera images
     */
    private fun standardizeFaceBitmap(faceBitmap: Bitmap): Bitmap {
        // PERBAIKAN: Gunakan ukuran yang sama dengan FaceProcessor (112x112)
        val standardWidth = 112  // Sama dengan IMAGE_SIZE di FaceProcessor
        val standardHeight = 112  // Sama dengan IMAGE_SIZE di FaceProcessor

        return try {
            // Resize to standard dimensions with high quality
            Bitmap.createScaledBitmap(faceBitmap, standardWidth, standardHeight, true)
        } catch (e: Exception) {
            println("Error standardizing face bitmap: ${e.message}")
            faceBitmap // Return original if standardization fails
        }
    }

    /**
     * Checks if a face is well-positioned for verification
     * @param face Detected face
     * @param imageWidth Width of the camera frame
     * @param imageHeight Height of the camera frame
     * @return true if face is properly positioned
     */
    fun isFaceWellPositioned(face: Face, imageWidth: Int, imageHeight: Int): Boolean {
        val boundingBox = face.boundingBox
        val faceWidth = boundingBox.width()
        val faceHeight = boundingBox.height()

        // Check if face is not too small or too large
        val minFaceSize = minOf(imageWidth, imageHeight) * 0.2f
        val maxFaceSize = minOf(imageWidth, imageHeight) * 0.8f
        val faceSize = minOf(faceWidth, faceHeight)

        if (faceSize < minFaceSize || faceSize > maxFaceSize) {
            return false
        }

        // Check if face is roughly centered
        val faceCenterX = boundingBox.centerX()
        val faceCenterY = boundingBox.centerY()
        val imageCenterX = imageWidth / 2f
        val imageCenterY = imageHeight / 2f

        val maxOffsetX = imageWidth * 0.25f
        val maxOffsetY = imageHeight * 0.25f

        return kotlin.math.abs(faceCenterX - imageCenterX) < maxOffsetX &&
                kotlin.math.abs(faceCenterY - imageCenterY) < maxOffsetY
    }

    /**
     * Clean up resources when done
     */
    fun release() {
        faceDetector.close()
    }
}
