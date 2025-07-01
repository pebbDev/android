package com.example.infinite_track.domain.model

data class MyLeaveModel(
    val id: Int,
    val name: String,
    val typeLeave: String,
    val dateTime : String,
    val cardImage : Int,
    val cardStatus : String,
)