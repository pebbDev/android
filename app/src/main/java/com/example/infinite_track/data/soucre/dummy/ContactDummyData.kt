package com.example.infinite_track.data.soucre.dummy

import com.example.infinite_track.domain.model.contact.ContactModel

object ContactDummyData {
    val dummyContacts = listOf(
        ContactModel(
            id = 1,
            fullName = "Budi Santoso",
            division = "Software Engineering",
            photoUrl = "https://res.cloudinary.com/dzs4n6jri/image/upload/v1719031264/profile1_rkxttk.jpg",
            phoneNumber = "6281234567890",
            email = "budi.santoso@company.com",
            whatsappNumber = "6281234567890"
        ),
        ContactModel(
            id = 2,
            fullName = "Siti Rahayu",
            division = "Human Resources",
            photoUrl = "https://res.cloudinary.com/dzs4n6jri/image/upload/v1719031264/profile2_rcmnnx.jpg",
            phoneNumber = "6285678901234",
            email = "siti.rahayu@company.com",
            whatsappNumber = "6285678901234"
        ),
        ContactModel(
            id = 3,
            fullName = "Ahmad Dani",
            division = "Product Management",
            photoUrl = "https://res.cloudinary.com/dzs4n6jri/image/upload/v1719031264/profile3_nvizun.jpg",
            phoneNumber = "6289012345678",
            email = "ahmad.dani@company.com",
            whatsappNumber = "6289012345678"
        )
    )
}
