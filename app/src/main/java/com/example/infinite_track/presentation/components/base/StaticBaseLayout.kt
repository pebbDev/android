package com.example.infinite_track.presentation.components.base

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.infinite_track.R
import com.example.infinite_track.presentation.theme.Infinite_TrackTheme

@Composable
fun StaticBaseLayout() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        Image(
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize(),
            painter = painterResource(
                id = R.drawable.background
            ),
            contentDescription = "Background"
        )
    }
}

@Preview
@Composable
private fun LayoutPreview() {
    Infinite_TrackTheme {
        StaticBaseLayout()
    }
}