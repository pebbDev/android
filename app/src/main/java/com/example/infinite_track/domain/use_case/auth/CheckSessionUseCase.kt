package com.example.infinite_track.domain.use_case.auth

import com.example.infinite_track.data.face.FaceProcessor
import com.example.infinite_track.domain.model.auth.UserModel
import com.example.infinite_track.domain.repository.AuthRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Use case for checking if user has an active session and syncing face embedding if needed
 */
class CheckSessionUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val faceProcessor: FaceProcessor
) {
    /**
     * Invokes the session check process
     * Orchestrates the flow of data between repository and face processor
     * @return Result<User> with the user data if session exists
     */
    suspend operator fun invoke(): Result<UserModel> {
        // First, sync user profile from the server
        val syncResult = authRepository.syncUserProfile()

        // If sync was successful, check if we need to update face embedding
        if (syncResult.isSuccess) {
            val newUserData = syncResult.getOrNull()!!

            // Get current user data from local storage to compare photoUpdatedAt
            val currentUser = authRepository.getLoggedInUser().first()

            // If photoUpdatedAt has changed or face embedding is missing, generate new embedding
            if (currentUser != null &&
                (newUserData.photoUpdatedAt != currentUser.photoUpdatedAt ||
                 currentUser.faceEmbedding == null)) {

                // Process face image to get embedding
                val embeddingResult = newUserData.photoUrl?.let { photoUrl ->
                    faceProcessor.generateEmbedding(photoUrl)
                } ?: Result.failure(NullPointerException("Photo URL is null"))

                // If embedding generation was successful, save it
                if (embeddingResult.isSuccess) {
                    val embedding = embeddingResult.getOrNull()!!
                    authRepository.saveFaceEmbedding(newUserData.id, embedding)
                }
            }
        }

        return syncResult
    }
}
