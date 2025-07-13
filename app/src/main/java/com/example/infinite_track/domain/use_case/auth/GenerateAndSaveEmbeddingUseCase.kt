package com.example.infinite_track.domain.use_case.auth

import com.example.infinite_track.data.face.FaceProcessor
import com.example.infinite_track.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Use case for generating face embedding from photo URL and saving it to database
 * Encapsulates the business logic for face embedding generation and persistence
 */
class GenerateAndSaveEmbeddingUseCase @Inject constructor(
    private val faceProcessor: FaceProcessor,
    private val authRepository: AuthRepository
) {
    /**
     * Generates face embedding from photo URL and saves it to database
     * @param userId User ID to associate with the embedding
     * @param photoUrl URL of the user's profile photo
     * @return Result<Unit> indicating success or failure of the operation
     */
    suspend operator fun invoke(userId: Int, photoUrl: String?): Result<Unit> {
        return try {
            // Validate input parameters
            if (photoUrl.isNullOrBlank()) {
                return Result.failure(IllegalArgumentException("Photo URL cannot be null or empty"))
            }

            // Generate face embedding using FaceProcessor
            val embeddingResult = faceProcessor.generateEmbedding(photoUrl)

            // Check if embedding generation was successful
            if (embeddingResult.isFailure) {
                return Result.failure(
                    embeddingResult.exceptionOrNull()
                        ?: Exception("Face embedding generation failed")
                )
            }

            // Get the generated embedding
            val embedding = embeddingResult.getOrNull()
                ?: return Result.failure(Exception("Generated embedding is null"))

            // Save embedding to database through repository
            authRepository.saveFaceEmbedding(userId, embedding)

            // Return success result
            Result.success(Unit)

        } catch (e: Exception) {
            // Return failure result with the caught exception
            Result.failure(e)
        }
    }
}
