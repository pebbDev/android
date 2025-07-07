package com.example.infinite_track.data.face

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.graphics.get
import androidx.core.graphics.scale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.tensorflow.lite.Interpreter
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
        private const val MODEL_FILE = "face_embeddings.tflite"
        private const val IMAGE_SIZE = 112 // Standard size for face recognition models
        private const val EMBEDDING_SIZE = 512 // Output size of the embedding vector
        private const val PIXEL_SIZE = 3 // RGB channels
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

    /**
     * Generates a face embedding from a profile photo URL
     * @param photoUrl URL of the user's profile photo
     * @return Result containing ByteArray embedding or error
     */
    suspend fun generateEmbedding(photoUrl: String): Result<ByteArray> =
        withContext(Dispatchers.IO) {
            try {
                if (photoUrl.isBlank()) {
                    return@withContext Result.failure(Exception("Photo URL is empty"))
                }

                val tflite = interpreter ?: return@withContext Result.failure(
                    Exception("TensorFlow Lite interpreter not initialized")
                )

                // 1. Download image from URL
                val bitmap = downloadImage(photoUrl) ?: return@withContext Result.failure(
                    Exception("Failed to download or process image")
                )

                // 2. Resize and preprocess the image
                val processedBitmap = preprocessImage(bitmap)

                // 3. Convert bitmap to input tensor
                val inputBuffer = convertBitmapToByteBuffer(processedBitmap)

                // 4. Run inference to get face embedding
                val outputBuffer = Array(1) { FloatArray(EMBEDDING_SIZE) }
                tflite.run(inputBuffer, outputBuffer)

                // 5. Convert FloatArray to ByteArray
                val embedding = convertFloatsToBytes(outputBuffer[0])

                // 6. Return success result with embedding
                Result.success(embedding)
            } catch (e: Exception) {
                Log.e(TAG, "Error generating face embedding", e)
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
     * Downloads and decodes an image from a URL
     */
    private suspend fun downloadImage(imageUrl: String): Bitmap? = withContext(Dispatchers.IO) {
        try {
            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()

            val inputStream = connection.inputStream
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
            connection.disconnect()

            bitmap
        } catch (e: IOException) {
            Log.e(TAG, "Error downloading image", e)
            null
        }
    }

    /**
     * Preprocesses the bitmap by resizing it to required dimensions
     */
    private fun preprocessImage(originalBitmap: Bitmap): Bitmap {
        return originalBitmap.scale(IMAGE_SIZE, IMAGE_SIZE)
    }

    /**
     * Converts a bitmap to a normalized byte buffer for model input
     */
    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val inputBuffer = ByteBuffer.allocateDirect(
            1 * IMAGE_SIZE * IMAGE_SIZE * PIXEL_SIZE * 4
        ).apply {
            order(ByteOrder.nativeOrder())
        }

        // Normalize pixel values to [0, 1]
        for (y in 0 until IMAGE_SIZE) {
            for (x in 0 until IMAGE_SIZE) {
                val pixel = bitmap[x, y]

                // Extract RGB values
                val r = (pixel shr 16 and 0xFF) / 255.0f
                val g = (pixel shr 8 and 0xFF) / 255.0f
                val b = (pixel and 0xFF) / 255.0f

                // Add normalized RGB values to buffer
                inputBuffer.putFloat(r)
                inputBuffer.putFloat(g)
                inputBuffer.putFloat(b)
            }
        }

        return inputBuffer
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
