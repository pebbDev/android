package com.example.infinite_track.data.soucre.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUserProfile(userEntity: UserEntity)

    @Query("SELECT * FROM user_profile LIMIT 1")
    fun getUserProfileFlow(): Flow<UserEntity?>

    @Query("SELECT * FROM user_profile LIMIT 1")
    suspend fun getUserProfile(): UserEntity?

    @Query("DELETE FROM user_profile")
    suspend fun clearUserProfile()
}
