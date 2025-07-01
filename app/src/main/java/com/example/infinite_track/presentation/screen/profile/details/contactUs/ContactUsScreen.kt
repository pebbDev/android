package com.example.infinite_track.presentation.screen.profile.details.contactUs

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.infinite_track.R
import com.example.infinite_track.presentation.components.button.InfiniteTracButtonBack
import com.example.infinite_track.presentation.core.body1
import com.example.infinite_track.presentation.core.body2
import com.example.infinite_track.presentation.core.headline4
import com.example.infinite_track.presentation.theme.Blue_500
import com.example.infinite_track.presentation.theme.Purple_400
import com.example.infinite_track.presentation.theme.Purple_500
import com.example.infinite_track.presentation.theme.Purple_600
import com.example.infinite_track.presentation.theme.White
import com.example.infinite_track.presentation.theme.sfCompact_font


@Composable
fun ContactUsScreen(
    onBackClick: () -> Unit,
) {
    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            InfiniteTracButtonBack(
                title = "Contact Us",
                navigationBack = onBackClick,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = stringResource(R.string.contact_us_description),
                    fontFamily = sfCompact_font,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = Purple_500
                )
                Spacer(modifier = Modifier.height(12.dp))

                AnimatedCard(
                    title = stringResource(R.string.customer_support),
                    rows = listOf(
                        Pair("Contact Number", "(0778) 123456"),
                        Pair(stringResource(R.string.email_address), "help@infinitetrack.com")
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                AnimatedCard(
                    title = stringResource(R.string.social_media),
                    rows = listOf(
                        Pair("Instagram", "@InfiniteTrack"),
                        Pair("X", "@InfiniteTrack"),
                        Pair("Facebook", "@InfiniteTrack")
                    )
                )
            }
        }
    }
}

@Composable
fun AnimatedCard(
    title: String,
    rows: List<Pair<String, String>>,
    modifier: Modifier = Modifier
) {
    // Interaction source for detecting press actions
    val mutableInteractionSource = remember { MutableInteractionSource() }
    val pressed = mutableInteractionSource.collectIsPressedAsState()

    // Animated border color
    val borderColor = animateColorAsState(
        targetValue = if (pressed.value) Blue_500 else Color.White,
        label = "BorderColor"
    )

    // Animated background color
    val backgroundColor = animateColorAsState(
        targetValue = if (pressed.value) White else Color.Transparent,
        label = "BackgroundColor"
    )

    // Animated shadow elevation
    val elevation = animateDpAsState(
        targetValue = if (pressed.value) 8.dp else 0.dp,
        label = "Elevation"
    )

    Box(
        modifier = modifier
            .padding(4.dp) // Ensure shadow is not clipped
            .shadow(
                elevation = elevation.value,
                shape = RoundedCornerShape(8.dp),
                clip = false,
                ambientColor = Blue_500.copy(alpha = 0.5f), // Shadow color
                spotColor = Blue_500 // Shadow color when pressed
            )
            .background(
                color = backgroundColor.value,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp, // Border thickness
                color = borderColor.value,
                shape = RoundedCornerShape(8.dp) // Border radius
            )
            .clickable(
                interactionSource = mutableInteractionSource,
                indication = null // Remove ripple effect
            ) { }
    ) {
        Card(
            colors = CardDefaults.cardColors(Color.Transparent),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = title,
                    style = headline4,
                    color = Purple_400
                )
                rows.forEach { row ->
                    Row(
                        modifier = Modifier.padding(top = 12.dp, start = 6.dp)
                    ) {
                        Icon(
                            painter = painterResource(
                                when (row.first) {
                                    "Contact Number" -> R.drawable.ic_phone
                                    "Instagram" -> R.drawable.ic_instagram
                                    "X" -> R.drawable.ic_mail
                                    "Facebook" -> R.drawable.ic_facebook
                                    else -> R.drawable.ic_mail
                                }
                            ),
                            contentDescription = row.first,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .align(Alignment.CenterVertically)
                                .size(height = 20.dp, width = 20.dp)
                        )
                        Column {
                            Text(
                                text = row.first,
                                style = body2,
                                color = Purple_400
                            )
                            Text(
                                text = row.second,
                                style = body1,
                                color = Purple_600
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun ContactUsPreview() {
    ContactUsScreen(
        onBackClick = {}
    )
}