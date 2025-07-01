package com.example.infinite_track.data.repository.profile

import com.example.infinite_track.data.mapper.auth.toDomain
import com.example.infinite_track.data.mapper.auth.toEntity
import com.example.infinite_track.data.soucre.local.room.UserDao
import com.example.infinite_track.data.soucre.network.request.ProfileUpdateRequest
import com.example.infinite_track.data.soucre.network.retrofit.ApiService
import com.example.infinite_track.domain.model.auth.UserModel
import com.example.infinite_track.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val userDao: UserDao
) : ProfileRepository {

    override suspend fun updateUserProfile(
        userId: Int,
        request: ProfileUpdateRequest
    ): Result<UserModel> {
        return try {
            val response = apiService.updateUserProfile(userId, request)

            if (response.success) {
                // Convert API response to domain model
                val updatedUserModel = response.data.toDomain()

                // Update local database with the new user data
                val currentUser = userDao.getUserProfile()
                val faceEmbedding = currentUser?.faceEmbedding
                userDao.insertOrUpdateUserProfile(updatedUserModel.toEntity(faceEmbedding))

                // Return success with updated model
                Result.success(updatedUserModel)
            } else {
                Result.failure(Exception(response.message ?: "Unknown error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getUserProfile(): Flow<UserModel?> {
        return userDao.getUserProfileFlow().map { userEntity ->
            userEntity?.toDomain()
        }
    }
}
