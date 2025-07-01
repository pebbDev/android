package com.example.infinite_track.domain.use_case.auth

import com.example.infinite_track.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Use case for handling user logout
 */
class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    /**
     * Invokes the logout process
     * @return Result<Unit> indicating success or failure
     */
    suspend operator fun invoke(): Result<Unit> {
        return authRepository.logout()
    }
}
