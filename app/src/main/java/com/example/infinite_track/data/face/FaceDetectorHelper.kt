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
 * Helper class for ML Kit Face Detection operations
 * Handles face detection, liveness verification (blink/smile), and face extraction
 */
@Singleton
class FaceDetectorHelper @Inject constructor() {

    companion object {
        private const val BLINK_THRESHOLD = 0.4f // Threshold for eye open probability
        private const val SMILE_THRESHOLD = 0.7f // Threshold for smile probability
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
     * Verifies if the person is blinking (liveness detection)
     * @param face Detected face from ML Kit
     * @return true if blink is detected (both eyes have low open probability)
     */
    fun verifyBlink(face: Face): Boolean {
        val leftEyeOpenProbability = face.leftEyeOpenProbability
        val rightEyeOpenProbability = face.rightEyeOpenProbability

        return if (leftEyeOpenProbability != null && rightEyeOpenProbability != null) {
            // Both eyes should have low open probability (indicating they are closed/blinking)
            leftEyeOpenProbability < BLINK_THRESHOLD && rightEyeOpenProbability < BLINK_THRESHOLD
        } else {
            false // Cannot determine blink if probabilities are not available
        }
    }

    /**
     * Verifies if the person is smiling (liveness detection)
     * @param face Detected face from ML Kit
     * @return true if smile is detected
     */
    fun verifySmile(face: Face): Boolean {
        val smilingProbability = face.smilingProbability
        return if (smilingProbability != null) {
            smilingProbability > SMILE_THRESHOLD
        } else {
            false // Cannot determine smile if probability is not available
        }
    }

    /**
     * Extracts face region from the full image bitmap
     * @param face Detected face with bounding box information
     * @param image Full image bitmap
     * @return Cropped face bitmap or null if extraction fails
     */
    fun extractFaceBitmap(face: Face, image: Bitmap): Bitmap? {
        return try {
            val boundingBox = face.boundingBox

            // Ensure bounding box is within image bounds
            val left = maxOf(0, boundingBox.left)
            val top = maxOf(0, boundingBox.top)
            val right = minOf(image.width, boundingBox.right)
            val bottom = minOf(image.height, boundingBox.bottom)

            // Validate that we have a valid crop area
            if (left < right && top < bottom) {
                Bitmap.createBitmap(image, left, top, right - left, bottom - top)
            } else {
                null
            }
        } catch (e: Exception) {
            null
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
