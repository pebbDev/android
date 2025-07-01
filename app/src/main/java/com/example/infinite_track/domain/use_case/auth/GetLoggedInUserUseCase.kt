package com.example.infinite_track.domain.use_case.auth

import com.example.infinite_track.domain.model.auth.UserModel
import com.example.infinite_track.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving the currently logged in user
 */
class GetLoggedInUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    /**
     * Invokes the use case to get the currently logged in user
     * @return Flow of UserModel that emits when the user data changes
     */
    operator fun invoke(): Flow<UserModel?> {
        return authRepository.getLoggedInUser()
    }
}
