package com.example.infinite_track.presentation.components.tittle

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.infinite_track.presentation.core.headline3
import com.example.infinite_track.presentation.theme.Blue_500

@Composable
fun Tittle(tittle: String) {
    Text(
        text = tittle,
        style = headline3,
    )
}
@Preview(showBackground = true)
@Composable
fun TitlePreview() {
    Tittle(tittle = "Infinite Track")
}