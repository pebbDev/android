package com.example.infinite_track.domain.use_case.auth

import android.graphics.Bitmap
import com.example.infinite_track.data.face.FaceProcessor
import com.example.infinite_track.domain.repository.AuthRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Use case for verifying face against stored embedding
 * Handles face comparison for attendance check-in/check-out
 */
class VerifyFaceUseCase @Inject constructor(
    private val faceProcessor: FaceProcessor,
    private val authRepository: AuthRepository
) {
    companion object {
        private const val SIMILARITY_THRESHOLD = 0.8f // Minimum similarity for face match
    }

    /**
     * Verifies captured face against stored user embedding
     * @param capturedFaceBitmap Bitmap of the captured face
     * @return Result<Boolean> indicating if face matches (true) or not (false)
     */
    suspend operator fun invoke(capturedFaceBitmap: Bitmap): Result<Boolean> {
        return try {
            // Get current user data with stored face embedding
            val currentUser = authRepository.getLoggedInUser().first()
                ?: return Result.failure(Exception("No logged in user found"))

            val storedEmbedding = currentUser.faceEmbedding
                ?: return Result.failure(Exception("No stored face embedding found. Please update your profile."))

            // Convert captured face to embedding using FaceProcessor
            // First, we need to save the bitmap temporarily and get a URL or process it directly
            val embeddingResult = generateEmbeddingFromBitmap(capturedFaceBitmap)

            if (embeddingResult.isFailure) {
                return Result.failure(
                    embeddingResult.exceptionOrNull()
                        ?: Exception("Failed to generate embedding from captured face")
                )
            }

            val capturedEmbedding = embeddingResult.getOrNull()!!

            // Compare embeddings using cosine similarity
            val similarity = calculateCosineSimilarity(storedEmbedding, capturedEmbedding)

            // Return true if similarity exceeds threshold
            val isMatch = similarity >= SIMILARITY_THRESHOLD

            Result.success(isMatch)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Generate embedding from bitmap (simplified version)
     * In a real implementation, you might need to save bitmap temporarily or modify FaceProcessor
     */
    private suspend fun generateEmbeddingFromBitmap(bitmap: Bitmap): Result<ByteArray> {
        return try {
            // For now, this is a placeholder
            // In production, you would need to modify FaceProcessor to accept Bitmap directly
            // or save the bitmap temporarily and use the existing generateEmbedding method

            // Placeholder implementation - you would replace this with actual bitmap processing
            Result.failure(Exception("Bitmap processing not implemented yet - needs FaceProcessor modification"))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Calculate cosine similarity between two embeddings
     * @param embedding1 First embedding (stored)
     * @param embedding2 Second embedding (captured)
     * @return Similarity score between 0.0 and 1.0
     */
    private fun calculateCosineSimilarity(embedding1: ByteArray, embedding2: ByteArray): Float {
        if (embedding1.size != embedding2.size) {
            return 0f
        }

        // Convert ByteArray to FloatArray for calculation
        val floats1 = byteArrayToFloatArray(embedding1)
        val floats2 = byteArrayToFloatArray(embedding2)

        var dotProduct = 0.0
        var normA = 0.0
        var normB = 0.0

        for (i in floats1.indices) {
            dotProduct += floats1[i] * floats2[i]
            normA += floats1[i] * floats1[i]
            normB += floats2[i] * floats2[i]
        }

        return if (normA == 0.0 || normB == 0.0) {
            0f
        } else {
            (dotProduct / (kotlin.math.sqrt(normA) * kotlin.math.sqrt(normB))).toFloat()
        }
    }

    /**
     * Convert ByteArray to FloatArray for similarity calculation
     */
    private fun byteArrayToFloatArray(byteArray: ByteArray): FloatArray {
        val floatArray = FloatArray(byteArray.size / 4)
        val buffer = java.nio.ByteBuffer.wrap(byteArray)
        buffer.order(java.nio.ByteOrder.LITTLE_ENDIAN)

        for (i in floatArray.indices) {
            floatArray[i] = buffer.getFloat(i * 4)
        }

        return floatArray
    }
}
