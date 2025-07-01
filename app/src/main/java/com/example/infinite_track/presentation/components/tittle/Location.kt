package com.example.infinite_track.presentation.components.tittle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.infinite_track.R
import com.example.infinite_track.presentation.core.body2
import com.example.infinite_track.presentation.theme.Infinite_TrackTheme
import com.example.infinite_track.presentation.theme.Purple_500
import com.example.infinite_track.presentation.theme.Violet_50

@Composable
fun Location(
    modifier: Modifier = Modifier,
    date: String,
    location: String
) {

    Row(
        modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = date,
            style = body2,
            color = Purple_500,
        )
        Box(
            modifier
                .background(Color(0xFF7B3DFF), RoundedCornerShape(16.dp))
                .padding(horizontal = 6.dp, vertical = 4.dp)
                .widthIn(max = 230.dp),

            ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_location),
                    contentDescription = "Location Icon",
                    tint = Color.White,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier.width(4.dp))
                Text(
                    text = location,
                    color = Violet_50,
                    style = body2,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.widthIn(max = 200.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun LocationPreview() {
    Infinite_TrackTheme {
        Location(date = "Monday, 09 September 2024", location = "Nongsa Digital, Indonesia")
    }
}