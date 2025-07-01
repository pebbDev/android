package com.example.infinite_track.presentation.components.button

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.infinite_track.presentation.core.headline3
import com.example.infinite_track.presentation.theme.Blue_500
import com.example.infinite_track.presentation.theme.Infinite_TrackTheme
import com.example.infinite_track.presentation.theme.Purple_500

@Composable
fun ButtonBack(
    modifier: Modifier = Modifier,
    title: String,
    navController: NavController
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = {
                navController.navigateUp()
            },
            modifier = Modifier
                .border(width = 1.dp, color = Blue_500, shape = RoundedCornerShape(size = 6.dp))
                .background(
                    Blue_500.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(size = 6.dp)
                )
                .size(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "Back",
                tint = Blue_500,
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            text = title,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = headline3,
            color = Purple_500,
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ButtonBackPreview() {
    Infinite_TrackTheme {
        ButtonBack(
            navController = rememberNavController(),
            title = "Sample Title"
        )
    }
}
