package com.example.infinite_track.presentation.components.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.infinite_track.R
import com.example.infinite_track.presentation.core.body1
import com.example.infinite_track.presentation.core.body2
import com.example.infinite_track.presentation.theme.Blue_500
import com.example.infinite_track.presentation.theme.Infinite_TrackTheme

@Composable
fun UserMyLeaveCard() {
    Card(
        border = CardDefaults.outlinedCardBorder(),
        colors = CardDefaults.cardColors(Color(0x33FFFFFF)),
        modifier = Modifier
            .width(433.dp)
            .height(250.dp),
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.profils),
                contentDescription = "Description of image",
                modifier = Modifier
                    .padding(top = 20.dp)
                    .size(height = 66.dp, width = 66.dp)
                    .clip(CircleShape)
                    .border(2.dp, color = Blue_500, CircleShape)
                    .shadow(20.dp, CircleShape)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = "Riyanda Azis Febrian", fontSize = 18.sp, style = body1)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Mobile Development", fontSize = 12.sp, style = body1, color = Color.Gray)

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Cuti Ikut Umroh", fontSize = 14.sp, style = body1)
            
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ){
                    Text(text = "Thu", style = body2, fontSize = 10.sp)
                    Text(text = "17 Sep 2024", style = body1, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.width(28.dp))

                Column{
                    Icon(imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight, contentDescription = "To")
                }

                Spacer(modifier = Modifier.width(28.dp))

                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ){
                    Text(text = "Fri", style = body2, fontSize = 10.sp)
                    Text(text = "17 Oct 2024", style = body1, fontSize = 14.sp)
                }

            }
        }
    }
}

@Preview
@Composable
fun UserMyLeaveCardPreview() {
    Infinite_TrackTheme {
        UserMyLeaveCard()
    }
}