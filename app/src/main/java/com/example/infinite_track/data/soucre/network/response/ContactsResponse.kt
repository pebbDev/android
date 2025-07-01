package com.example.infinite_track.data.soucre.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContactsResponse(
    @field:SerializedName("contactData")
    val contactData: List<ContactData?>? = null
): Parcelable

@Parcelize
data class ContactData(
    @field:SerializedName("actions")
    val actions: Actions,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("positionId")
    val positionId: Int? = null,

    @field:SerializedName("positionName")
    val positionName: String? = null,

    @field:SerializedName("profile_photo")
    val profilePhoto: String? = null,

    @field:SerializedName("userId")
    val userId: Int? = null
): Parcelable

@Parcelize
data class Actions(
    @field:SerializedName("call")
    val call: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("whatsapp")
    val whatsapp: String? = null
): Parcelable
