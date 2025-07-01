package com.example.infinite_track.presentation.components.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.infinite_track.R
import com.example.infinite_track.presentation.components.modifiers.basicMarquee
import com.example.infinite_track.presentation.core.body3
import com.example.infinite_track.presentation.core.bodyThin
import com.example.infinite_track.presentation.core.headline1
import com.example.infinite_track.presentation.core.headline4
import com.example.infinite_track.presentation.theme.Purple_300
import com.example.infinite_track.presentation.theme.Purple_500

@Composable
fun BarStatus(
    progressBar: Float,
    height: Dp = 5.dp,
    backgroundColor: Color = Color(0xFFAD77FF),
    progressColor: Color = MaterialTheme.colorScheme.primary,
    cornerRadius: Dp = 25.dp,

    ) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(cornerRadius),
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progressBar)
                .background(progressColor, shape = RoundedCornerShape(cornerRadius))
        )
    }
}

@Composable
fun MenuCard(
    modifier: Modifier = Modifier,
    annualBalance: Int?,
    annualUsed: Int?,
    annualLeft: Int,
    position: String,
    userName: String,
    greeting: String,
    profileImage: String,
    onClickLiveAttendance: () -> Unit,
    onClickTimeOff: () -> Unit

) {
    val progress = if (annualBalance != null && annualBalance > 0 && annualUsed != null) {
        (annualUsed.toFloat() / annualBalance.toFloat()).coerceIn(0f, 1f)
    } else {
        0f
    }

    Column(
        modifier = modifier
            .background(
                shape = RoundedCornerShape(25.dp),
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFFFFFFF),
                        Color(0xFFFFFFFF),
                        Color(0xFFFFDE70)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(400f, 1000f)
                ),
                alpha = 0.5f
            )
            .border(width = 2.dp, color = Color(0xFFFFFFFF), shape = RoundedCornerShape(25.dp)),
    ) {
        Column(
            modifier = modifier
                .padding(10.dp),
        ) {
            Text(
                text = greeting,
                style = headline4
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
                            style = headline1,
                            color = Purple_500,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = position,
                            style = headline4,
                            color = Purple_300,
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
                        model = profileImage,
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
            Column(
                modifier = modifier
                    .padding(top = 12.dp),
            ) {
                Text(
                    text = "Available Leave Days : ",
                    style = bodyThin
                )
                if (annualBalance === null) {
                    Text(
                        text = "0 Days",
                        color = Purple_500,
                        fontSize = 27.sp
                    )
                } else if (annualBalance != null) {
                    Text(
                        text = "${annualBalance - annualUsed!!} Days",
                        color = Purple_500,
                        fontSize = 27.sp
                    )
                }
                if (annualBalance === null) {
                    Text(
                        text = "0 Used Days, 0 Remaining Days",
                        style = bodyThin,
                        modifier = modifier
                            .padding(top = 6.dp)
                            .padding(bottom = 6.dp),
                    )
                } else if (annualBalance != null) {
                    Text(
                        text = "${annualUsed} Used Days, ${annualBalance - annualUsed!!} Remaining Days",
                        style = bodyThin,
                        modifier = modifier
                            .padding(top = 6.dp)
                            .padding(bottom = 6.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            BarStatus(
                progressBar = progress,
                height = 8.dp,
                cornerRadius = 25.dp
            )

            Row(
                modifier = modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(12.dp)
                            .height(4.dp)
                            .background(Color.Gray.copy(alpha = 0.6f), shape = CircleShape)
                    )

                    Box(
                        modifier = Modifier
                            .size(4.dp)
                            .background(Color.Gray.copy(alpha = 0.4f), shape = CircleShape)
                    )

                    Box(
                        modifier = Modifier
                            .size(4.dp)
                            .background(Color.Gray.copy(alpha = 0.4f), shape = CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .background(
                        shape = RoundedCornerShape(15.dp),
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF3F3679),
                                Color(0xFFFFFFFF)
                            ),
                            start = Offset(0f, 0f),
                            end = Offset(400f, 900f)
                        ),
                        alpha = 0.1f
                    )
                    .fillMaxWidth()
                    .height(90.dp) // Tetapkan tinggi tetap agar tidak berubah sesuai konten
                    .border(
                        width = 2.dp,
                        color = Color(0xFFFFFFFF),
                        shape = RoundedCornerShape(15.dp)
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically // Align vertical center
            ) {

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(5.dp)
                        .clickable {
                            onClickLiveAttendance()
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.liveatt2),
                            contentDescription = "Live Attendance Icon",
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.live_attendance),
                        style = body3,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(5.dp)
                        .clickable { onClickTimeOff() },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.timeoff),
                            contentDescription = "Time-Off Request Icon",
                            modifier = Modifier
                                .size(40.dp) // Tetapkan ukuran tetap
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.time_off_request),
                        style = body3,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 4.dp)
                    )
                }
            }
        }
    }
}
