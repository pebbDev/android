package com.example.infinite_track.domain.use_case.auth

import com.example.infinite_track.data.face.FaceProcessor
import com.example.infinite_track.data.soucre.network.request.LoginRequest
import com.example.infinite_track.domain.model.auth.UserModel
import com.example.infinite_track.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Use case for handling user login and initial face embedding generation
 */
class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val faceProcessor: FaceProcessor
) {
    /**
     * Invokes the login process with email and password
     * Orchestrates the flow between repository and face processor
     * @return Result<User> with the user data if successful
     */
    suspend operator fun invoke(email: String, password: String): Result<UserModel> {
        // First, perform the login via repository
        val loginRequest = LoginRequest(email, password)
        val loginResult = authRepository.login(loginRequest)

        // If login is successful, process the user's photo to generate face embedding
        if (loginResult.isSuccess) {
            val userData = loginResult.getOrNull()!!

            // Only process if photo URL is available
            if (userData.photoUrl?.isNotBlank() == true) {
                // Generate embedding from user's photo
                userData.photoUrl?.let { photoUrl ->
                    // Generate embedding from user's photo
                    val embeddingResult = faceProcessor.generateEmbedding(photoUrl)

                    // If embedding generation is successful, save it to the repository
                    if (embeddingResult.isSuccess) {
                        val embedding = embeddingResult.getOrNull()!!
                        authRepository.saveFaceEmbedding(userData.id, embedding)
                    }
                }
            }
        }

        // Return the original login result
        return loginResult
    }
}
