package com.example.infinite_track.presentation.components.tittle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.infinite_track.R
import com.example.infinite_track.presentation.components.modifiers.basicMarquee
import com.example.infinite_track.presentation.theme.Purple_300
import com.example.infinite_track.presentation.theme.Purple_400
import com.example.infinite_track.presentation.theme.Purple_500

@Composable
fun nameCards(
    modifier: Modifier = Modifier,
    greeting: String,
    userName: String,
    division: String,
    profileImage: String?,

    ) {
    val fontss = FontFamily(
        Font(R.font.sf_compact_bold, FontWeight.Bold),
        Font(R.font.sf_compact_thin, FontWeight.Thin),
        Font(R.font.sf_compact_italic, FontWeight.Light),
        Font(R.font.sf_compact_medium, FontWeight.Medium),
    )
    Column {
        Text(
            text = greeting,
            color = Purple_400,
            fontFamily = fontss,
            fontWeight = FontWeight.Thin
        )
        Row(
            modifier = modifier,
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .width(250.dp)
                ) {
                    Text(
                        text = userName,
                            fontSize = 27.sp,
                        fontFamily = fontss,
                        fontWeight = FontWeight.Medium,
                        color = Purple_500,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = division,
                        fontSize = 16.sp,
                        color = Purple_300,
                        fontFamily = fontss,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.basicMarquee(
                            delayMillis = 1200,
                            velocityMultiplier = 0.8f
                        )
                    )
                }
                AsyncImage(
                    model = profileImage ?: R.drawable.ic_profile,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .height(50.dp)
                        .width(50.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}