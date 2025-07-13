package com.example.infinite_track.data.soucre.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserEntity(
    @PrimaryKey
    val id: Int,
    val fullName: String,
    val email: String,
    val roleName: String,
    val positionName: String?,
    val programName: String?,
    val divisionName: String?,
    val nipNim: String,
    val phone: String?,
    val photo: String?,
    val photoUpdatedAt: String?,
    val latitude: Double?,
    val longitude: Double?,
    val radius: Int?,
    val locationDescription: String?,
    val locationCategoryName: String?,
    val faceEmbedding: ByteArray? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserEntity

        if (id != other.id) return false
        if (fullName != other.fullName) return false
        if (email != other.email) return false
        if (roleName != other.roleName) return false
        if (positionName != other.positionName) return false
        if (programName != other.programName) return false
        if (divisionName != other.divisionName) return false
        if (nipNim != other.nipNim) return false
        if (phone != other.phone) return false
        if (photo != other.photo) return false
        if (photoUpdatedAt != other.photoUpdatedAt) return false
        if (latitude != other.latitude) return false
        if (longitude != other.longitude) return false
        if (radius != other.radius) return false
        if (locationDescription != other.locationDescription) return false
        if (locationCategoryName != other.locationCategoryName) return false
        if (faceEmbedding != null) {
            if (other.faceEmbedding == null) return false
            if (!faceEmbedding.contentEquals(other.faceEmbedding)) return false
        } else if (other.faceEmbedding != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + fullName.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + roleName.hashCode()
        result = 31 * result + (positionName?.hashCode() ?: 0)
        result = 31 * result + (programName?.hashCode() ?: 0)
        result = 31 * result + (divisionName?.hashCode() ?: 0)
        result = 31 * result + nipNim.hashCode()
        result = 31 * result + (phone?.hashCode() ?: 0)
        result = 31 * result + (photo?.hashCode() ?: 0)
        result = 31 * result + (photoUpdatedAt?.hashCode() ?: 0)
        result = 31 * result + (latitude?.hashCode() ?: 0)
        result = 31 * result + (longitude?.hashCode() ?: 0)
        result = 31 * result + (radius ?: 0)
        result = 31 * result + (locationDescription?.hashCode() ?: 0)
        result = 31 * result + (locationCategoryName?.hashCode() ?: 0)
        result = 31 * result + (faceEmbedding?.contentHashCode() ?: 0)
        return result
    }
}
