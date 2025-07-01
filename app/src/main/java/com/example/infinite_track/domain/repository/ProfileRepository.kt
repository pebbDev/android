package com.example.infinite_track.domain.repository

import com.example.infinite_track.data.soucre.network.request.ProfileUpdateRequest
import com.example.infinite_track.domain.model.auth.UserModel

/**
 * Repository interface for user profile operations
 */
interface ProfileRepository {
    /**
     * Updates the user profile information
     * @param userId The ID of the user to update
     * @param request The profile update data
     * @return Result containing the updated user or an error
     */
    suspend fun updateUserProfile(userId: Int, request: ProfileUpdateRequest): Result<UserModel>

    /**
     * Gets the user profile information from local storage
     * @return Flow of UserModel
     */
    fun getUserProfile(): kotlinx.coroutines.flow.Flow<UserModel?>
}
