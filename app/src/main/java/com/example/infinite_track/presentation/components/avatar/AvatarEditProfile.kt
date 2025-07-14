package com.example.infinite_track.presentation.components.avatar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.infinite_track.R
import com.example.infinite_track.presentation.core.headline2
import com.example.infinite_track.presentation.theme.Purple_400
import com.example.infinite_track.presentation.theme.Purple_500

@Composable
fun ProfileCard(
    imageResId: Int,
    name: String,
    jobTitle: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier.size(100.dp)
        ) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.White, CircleShape),
                contentScale = ContentScale.Crop
            )
            Image(
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = "Edit Icon",
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF6200EA))
            )
        }


        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = name,
            fontSize = 20.sp,
            style = headline2,
            color = Purple_500
        )
        Text(
            text = jobTitle,
            fontSize = 16.sp,
            color = Purple_400
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileCard() {
    ProfileCard(
        imageResId = R.drawable.ic_avatareditprofile,
        name = "Mhmd Febriyadi",
        jobTitle = "Mobile Development"
    )
}
