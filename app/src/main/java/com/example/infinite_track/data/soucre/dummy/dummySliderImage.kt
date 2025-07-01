package com.example.infinite_track.data.soucre.dummy

data class SliderItem(
    val imageUrl: String,
    val title: String
)

val dummySliderItems = listOf(
    SliderItem(
        imageUrl = "https://res.cloudinary.com/dfbcj6o7j/image/upload/v1749550042/image_slider_two_bdclz5.jpg",
        title = "Infinite Learning"
    ),
    SliderItem(
        imageUrl = "https://res.cloudinary.com/dfbcj6o7j/image/upload/v1749550041/image_slider_one_bee3ej.jpg",
        title = "Altera Academy"
    ),
    SliderItem(
        imageUrl = "https://res.cloudinary.com/dfbcj6o7j/image/upload/v1749550039/image_slider_three_lv5tpr.jpg",
        title = "Bangkit Academy"
    ),
    SliderItem(
        imageUrl = "https://res.cloudinary.com/dfbcj6o7j/image/upload/v1749550039/image_slider_three_lv5tpr.jpg",
        title = "Dicoding"
    )
)