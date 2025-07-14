package com.example.infinite_track.data.face

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import java.io.FileInputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Processor for generating face embeddings from profile photos
 * Encapsulates all TensorFlow Lite operations for face recognition
 */
@Singleton
class FaceProcessor @Inject constructor(
    private val appContext: Context
) {
    companion object {
        private const val TAG = "FaceProcessor"
        private const val MODEL_FILE = "face_recognition_metadata.tflite"
        private const val IMAGE_SIZE = 112 // Standard size for face recognition models
        private const val EMBEDDING_SIZE = 128 // Output size of the embedding vector
    }

    // Lazy-loaded TensorFlow Lite Interpreter
    private val interpreter by lazy {
        try {
            Interpreter(loadModelFile())
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing TFLite interpreter", e)
            null
        }
    }

    // ImageProcessor for handling resize and normalization operations
    private val imageProcessor by lazy {
        ImageProcessor.Builder()
            .add(ResizeOp(IMAGE_SIZE, IMAGE_SIZE, ResizeOp.ResizeMethod.BILINEAR))
            .add(NormalizeOp(127.5f, 127.5f)) // Normalize from [0,255] to [-1,1]
            .build()
    }

    /**
     * Generates a face embedding from a profile photo URL
     * @param photoUrl URL of the user's profile photo
     * @return Result containing ByteArray embedding or error
     */
    suspend fun generateEmbedding(photoUrl: String): Result<ByteArray> =
        withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Starting generateEmbedding for URL: $photoUrl")

                if (photoUrl.isBlank()) {
                    Log.e(TAG, "Photo URL is empty")
                    return@withContext Result.failure(Exception("Photo URL is empty"))
                }

                val tflite = interpreter ?: return@withContext Result.failure(
                    Exception("TensorFlow Lite interpreter not initialized")
                )

                Log.d(TAG, "TensorFlow Lite interpreter initialized successfully")

                // 1. Download image from URL
                Log.d(TAG, "Downloading image from URL: $photoUrl")
                val bitmap = downloadImage(photoUrl)

                if (bitmap == null) {
                    Log.e(
                        TAG,
                        "Failed to download or decode image from URL: $photoUrl"
                    )
                    return@withContext Result.failure(
                        Exception("Failed to download or process image from URL: $photoUrl")
                    )
                }

                Log.d(
                    TAG,
                    "Image downloaded successfully, size: ${bitmap.width}x${bitmap.height}"
                )

                // 2. Process image using ImageProcessor (resize and normalize)
                Log.d(
                    TAG,
                    "Processing image (resize to ${IMAGE_SIZE}x${IMAGE_SIZE} and normalize)"
                )
                val tensorImage = TensorImage.fromBitmap(bitmap)
                val processedImage = imageProcessor.process(tensorImage)
                Log.d(TAG, "Image processed successfully")

                // 3. Run inference to get face embedding
                Log.d(TAG, "Running TensorFlow Lite inference")
                val outputBuffer = Array(1) { FloatArray(EMBEDDING_SIZE) }

                try {
                    tflite.run(processedImage.buffer, outputBuffer)
                    Log.d(TAG, "TensorFlow Lite inference completed successfully")
                } catch (e: IllegalArgumentException) {
                    Log.e(
                        TAG,
                        "Tensor shape mismatch during inference. Expected output: [1, $EMBEDDING_SIZE]",
                        e
                    )
                    return@withContext Result.failure(
                        Exception("Model output size mismatch. Expected [$EMBEDDING_SIZE] dimensions, but got different size.")
                    )
                }

                // 4. Convert FloatArray to ByteArray
                Log.d(TAG, "Converting float array to byte array")
                val embedding = convertFloatsToBytes(outputBuffer[0])
                Log.d(
                    TAG,
                    "Face embedding generated successfully, final size: ${embedding.size} bytes"
                )

                // 5. Return success result with embedding
                Result.success(embedding)
            } catch (e: Exception) {
                Log.e(TAG, "Error generating face embedding from URL: $photoUrl", e)
                Result.failure(e)
            }
        }

    /**
     * Generates a face embedding directly from a Bitmap
     * @param bitmap Bitmap of the face image
     * @return Result containing ByteArray embedding or error
     */
    suspend fun generateEmbeddingFromBitmap(bitmap: Bitmap): Result<ByteArray> =
        withContext(Dispatchers.IO) {
            try {
                val tflite = interpreter ?: return@withContext Result.failure(
                    Exception("TensorFlow Lite interpreter not initialized")
                )

                // Process bitmap using ImageProcessor (resize and normalize)
                val tensorImage = TensorImage.fromBitmap(bitmap)
                val processedImage = imageProcessor.process(tensorImage)

                // Run inference to get face embedding
                val outputBuffer = Array(1) { FloatArray(EMBEDDING_SIZE) }

                try {
                    tflite.run(processedImage.buffer, outputBuffer)
                } catch (e: IllegalArgumentException) {
                    // Handle the tensor shape mismatch error
                    Log.e(TAG, "Tensor shape mismatch. Expected output size: $EMBEDDING_SIZE", e)
                    return@withContext Result.failure(
                        Exception("Model output size mismatch. Expected $EMBEDDING_SIZE dimensions.")
                    )
                }

                // Convert FloatArray to ByteArray
                val embedding = convertFloatsToBytes(outputBuffer[0])

                // Log success for debugging
                Log.d(TAG, "Successfully generated embedding of size: ${embedding.size}")

                Result.success(embedding)
            } catch (e: Exception) {
                Log.e(TAG, "Error generating face embedding from bitmap", e)
                Result.failure(e)
            }
        }

    /**
     * Loads the TensorFlow Lite model file from assets
     */
    private fun loadModelFile(): MappedByteBuffer {
        val fileDescriptor = appContext.assets.openFd(MODEL_FILE)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    /**
     * Downloads and decodes an image from a URL with SSL error handling
     */
    private suspend fun downloadImage(imageUrl: String): Bitmap? = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Attempting to download image from: $imageUrl")

            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection

            // Configure connection with better SSL handling
            connection.apply {
                doInput = true
                connectTimeout = 15000 // 15 seconds timeout
                readTimeout = 30000 // 30 seconds timeout
                requestMethod = "GET"

                // Add user agent to avoid blocking
                setRequestProperty("User-Agent", "Mozilla/5.0 (Android)")
                setRequestProperty("Accept", "image/*")
                setRequestProperty("Connection", "close")
            }

            Log.d(TAG, "Connecting to image URL...")
            connection.connect()

            val responseCode = connection.responseCode
            Log.d(TAG, "HTTP Response Code: $responseCode")

            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()
                connection.disconnect()

                if (bitmap != null) {
                    Log.d(
                        TAG,
                        "Image downloaded successfully: ${bitmap.width}x${bitmap.height}"
                    )
                } else {
                    Log.e(TAG, "Failed to decode bitmap from input stream")
                }

                bitmap
            } else {
                Log.e(TAG, "HTTP Error: $responseCode - ${connection.responseMessage}")
                connection.disconnect()
                null
            }

        } catch (e: javax.net.ssl.SSLHandshakeException) {
            Log.e(TAG, "SSL Handshake failed. Trying alternative approach...", e)

            // Try alternative approach for SSL issues
            downloadImageWithRetry(imageUrl)

        } catch (e: IOException) {
            Log.e(TAG, "IO Error downloading image from: $imageUrl", e)
            null
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error downloading image from: $imageUrl", e)
            null
        }
    }

    /**
     * Alternative download method with retry mechanism
     */
    private suspend fun downloadImageWithRetry(imageUrl: String): Bitmap? =
        withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Attempting alternative download method for: $imageUrl")

                // Create URL connection with different approach
                val url = URL(imageUrl)
                val connection = url.openConnection()

                connection.apply {
                    connectTimeout = 20000
                    readTimeout = 40000
                    setRequestProperty("User-Agent", "Android-App/1.0")
                }

                val inputStream = connection.getInputStream()
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()

                if (bitmap != null) {
                    Log.d(
                        TAG,
                        "Alternative download successful: ${bitmap.width}x${bitmap.height}"
                    )
                }

                bitmap

            } catch (e: Exception) {
                Log.e(TAG, "Alternative download also failed", e)
                null
            }
        }


    /**
     * Converts a FloatArray embedding to a ByteArray for storage
     */
    private fun convertFloatsToBytes(floats: FloatArray): ByteArray {
        val buffer = ByteBuffer.allocate(floats.size * 4)
        buffer.order(ByteOrder.LITTLE_ENDIAN)

        for (f in floats) {
            buffer.putFloat(f)
        }

        return buffer.array()
    }
}