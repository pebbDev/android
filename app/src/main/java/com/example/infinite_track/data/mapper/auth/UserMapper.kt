package com.example.infinite_track.data.mapper.auth

import com.example.infinite_track.data.soucre.local.room.UserEntity
import com.example.infinite_track.data.soucre.network.response.UserData
import com.example.infinite_track.domain.model.auth.UserModel

/**
 * Extension function to convert UserData DTO from network response to domain User model
 * Maps all fields including nested location data with safe handling of nulls
 */
fun UserData.toDomain(): UserModel {
    return UserModel(
        id = this.id,
        fullName = this.fullName,
        email = this.email,
        roleName = this.roleName,
        positionName = this.positionName,
        programName = this.programName,
        divisionName = this.divisionName,
        nipNim = this.nipNim,
        phone = this.phone,
        photoUrl = this.photo,
        photoUpdatedAt = this.photoUpdatedAt,
        latitude = this.location?.latitude,
        longitude = this.location?.longitude,
        radius = this.location?.radius,
        locationDescription = this.location?.description,
        locationCategoryName = this.location?.categoryName,
        // Face embedding is null from API
        faceEmbedding = null
    )
}

/**
 * Extension function to convert room UserEntity to domain User model
 * Maps all fields directly as they now match between the two models
 */
fun UserEntity.toDomain(): UserModel {
    return UserModel(
        id = this.id,
        fullName = this.fullName,
        email = this.email,
        roleName = this.roleName,
        positionName = this.positionName,
        programName = this.programName,
        divisionName = this.divisionName,
        nipNim = this.nipNim,
        phone = this.phone,
        photoUrl = this.photo,
        photoUpdatedAt = this.photoUpdatedAt,
        latitude = this.latitude,
        longitude = this.longitude,
        radius = this.radius,
        locationDescription = this.locationDescription,
        locationCategoryName = this.locationCategoryName,
        faceEmbedding = this.faceEmbedding
    )
}

/**
 * Extension function to convert domain User model to room UserEntity
 * Maps all fields directly using domain model values
 * Preserves face embedding data if available
 */
fun UserModel.toEntity(currentEmbedding: ByteArray?): UserEntity {
    return UserEntity(
        id = this.id,
        fullName = this.fullName,
        email = this.email,
        roleName = this.roleName,
        positionName = this.positionName,
        programName = this.programName,
        divisionName = this.divisionName,
        nipNim = this.nipNim,
        phone = this.phone,
        photo = this.photoUrl,
        photoUpdatedAt = this.photoUpdatedAt,
        latitude = this.latitude,
        longitude = this.longitude,
        radius = this.radius,
        locationDescription = this.locationDescription,
        locationCategoryName = this.locationCategoryName,
        faceEmbedding = currentEmbedding ?: this.faceEmbedding
    )
}
