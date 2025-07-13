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
        private const val MODEL_FILE = "face_embeddings.tflite"
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

                // 2. Process image using ImageProcessor (resize and normalize)
                val tensorImage = TensorImage.fromBitmap(bitmap)
                val processedImage = imageProcessor.process(tensorImage)

                // 3. Run inference to get face embedding
                val outputBuffer = Array(1) { FloatArray(EMBEDDING_SIZE) }
                tflite.run(processedImage.buffer, outputBuffer)

                // 4. Convert FloatArray to ByteArray
                val embedding = convertFloatsToBytes(outputBuffer[0])

                // 5. Return success result with embedding
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