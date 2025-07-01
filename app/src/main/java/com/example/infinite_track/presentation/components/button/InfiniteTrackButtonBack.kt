package com.example.infinite_track.presentation.components.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.infinite_track.R
import com.example.infinite_track.presentation.core.headline3
import com.example.infinite_track.presentation.theme.Blue_500
import com.example.infinite_track.presentation.theme.Infinite_TrackTheme
import com.example.infinite_track.presentation.theme.Purple_500

@Composable
fun InfiniteTracButtonBack(
    modifier: Modifier = Modifier,
    title: String,
    navigationBack: () -> Unit
) {
    Box(
        modifier = modifier.fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
//        IconButton(
//            onClick = {
//                navigationBack()
//            },
//        ) {
//            Icon(
//                imageVector = Icons.Default.KeyboardArrowLeft,
//                contentDescription = "Back",
//                tint = Blue_500,
//                modifier = Modifier.size(24.dp)
//            )
//        }
        Image(
            painter = painterResource(id = R.drawable.ic_backks),
            contentDescription = "Icon CheckIn",
            modifier = Modifier
                .size(28.dp)
                .clickable { navigationBack() },
        )

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
fun InfiniteTracButtonBackPreview() {
    Infinite_TrackTheme {
        InfiniteTracButtonBack(
            navigationBack = { /* Handle navigation back */ },
            title = "Sample Title"
        )
    }
}
