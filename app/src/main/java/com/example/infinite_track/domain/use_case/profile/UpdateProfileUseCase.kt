package com.example.infinite_track.domain.use_case.profile

import com.example.infinite_track.data.soucre.network.request.ProfileUpdateRequest
import com.example.infinite_track.domain.model.auth.UserModel
import com.example.infinite_track.domain.repository.ProfileRepository
import javax.inject.Inject

/**
 * Use case for updating user profile information
 */
class UpdateProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    /**
     * Updates the user profile with the provided information
     * @param userId The ID of the user to update
     * @param request The profile update data
     * @return Result containing the updated user or an error
     */
    suspend operator fun invoke(userId: Int, request: ProfileUpdateRequest): Result<UserModel> {
        return profileRepository.updateUserProfile(userId, request)
    }
}
