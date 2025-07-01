package com.example.infinite_track.domain.model.contact

data class ContactModel(
    val id: Int,
    val fullName: String,
    val division: String,
    val photoUrl: String,
    val phoneNumber: String,
    val email: String,
    val whatsappNumber: String
)
