package com.example.infinite_track.data.soucre.dummy

import com.example.infinite_track.R

data class Inbox(
    val id: Int,
    val DateTime: String,
    val Name: String,
    val TypeLeave: String,
    val CardImage: Int,
    val CardStatus: String,
    val AvailableDays: String,
    val onClick: () -> Unit,
    val onDelete: () -> Unit
)

val dummyTimeOff = listOf(
    Inbox(
        id = 1,
        DateTime = "11 Aug 2023, 19:39",
        Name = "Raja Muhammad Farhan Zahputra",
        TypeLeave = "Cuti Melahirkan",
        CardImage = R.drawable.ic_profile,
        CardStatus = "Decline",
        AvailableDays = "3 days",
        onClick = { println("Card 1 clicked!") },
        onDelete = { println("Card 1 deleted!") }
    ),
    Inbox(
        id = 2,
        DateTime = "15 Feb 2024, 10:15",
        Name = "Dewi Kartika Sari",
        TypeLeave = "Cuti Tahunan",
        CardImage = android.R.drawable.ic_menu_report_image,
        CardStatus = "Approve",
        AvailableDays = "5 days",
        onClick = { println("Card 2 clicked!") },
        onDelete = { println("Card 2 deleted!") }
    ),
    Inbox(
        id = 3,
        DateTime = "20 Feb 2024, 08:25",
        Name = "Indra Saputra",
        TypeLeave = "Cuti Sakit",
        CardImage = android.R.drawable.ic_menu_report_image,
        CardStatus = "Pending",
        AvailableDays = "2 days",
        onClick = { println("Card 3 clicked!") },
        onDelete = { println("Card 3 deleted!") }
    ),
    Inbox(
        id = 4,
        DateTime = "12 March 2024, 14:45",
        Name = "Siti Nur Aisyah",
        TypeLeave = "Cuti Umroh",
        CardImage = R.drawable.ic_profile,
        CardStatus = "Decline",
        AvailableDays = "4 days",
        onClick = { println("Card 4 clicked!") },
        onDelete = { println("Card 4 deleted!") }
    ),
    Inbox(
        id = 5,
        DateTime = "03 April 2024, 11:30",
        Name = "Wawan Hermawan",
        TypeLeave = "Cuti Melahirkan",
        CardImage = android.R.drawable.ic_menu_report_image,
        CardStatus = "Approve",
        AvailableDays = "6 days",
        onClick = { println("Card 5 clicked!") },
        onDelete = { println("Card 5 deleted!") }
    ),
    Inbox(
        id = 6,
        DateTime = "08 May 2024, 16:55",
        Name = "Teguh Riyadi",
        TypeLeave = "Cuti Nikah",
        CardImage = R.drawable.ic_profile,
        CardStatus = "Pending",
        AvailableDays = "1 day",
        onClick = { println("Card 6 clicked!") },
        onDelete = { println("Card 6 deleted!") }
    ),
    Inbox(
        id = 7,
        DateTime = "22 May 2024, 09:20",
        Name = "Fitri Ayu Permatasari",
        TypeLeave = "Cuti Tahunan",
        CardImage = android.R.drawable.ic_menu_report_image,
        CardStatus = "Approve",
        AvailableDays = "7 days",
        onClick = { println("Card 7 clicked!") },
        onDelete = { println("Card 7 deleted!") }
    ),
    Inbox(
        id = 8,
        DateTime = "30 June 2024, 18:40",
        Name = "Budi Santoso",
        TypeLeave = "Cuti Ayah",
        CardImage = R.drawable.ic_profile,
        CardStatus = "Decline",
        AvailableDays = "2 days",
        onClick = { println("Card 8 clicked!") },
        onDelete = { println("Card 8 deleted!") }
    ),
    Inbox(
        id = 9,
        DateTime = "12 July 2024, 15:00",
        Name = "Anita Lestari",
        TypeLeave = "Cuti Sakit",
        CardImage = android.R.drawable.ic_menu_report_image,
        CardStatus = "Approve",
        AvailableDays = "3 days",
        onClick = { println("Card 9 clicked!") },
        onDelete = { println("Card 9 deleted!") }
    ),
    Inbox(
        id = 10,
        DateTime = "19 August 2024, 20:10",
        Name = "Agus Susanto",
        TypeLeave = "Cuti Duka",
        CardImage = R.drawable.ic_profile,
        CardStatus = "Pending",
        AvailableDays = "4 days",
        onClick = { println("Card 10 clicked!") },
        onDelete = { println("Card 10 deleted!") }
    )
)
