package com.example.infinite_track.presentation.screen.leave_request.timeOff

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.infinite_track.R
import com.example.infinite_track.data.soucre.dummy.Inbox
import com.example.infinite_track.data.soucre.dummy.dummyTimeOff
import com.example.infinite_track.presentation.components.button.TabManageButton
import com.example.infinite_track.presentation.components.cards.CardInbox
import com.example.infinite_track.presentation.core.headline2

@Composable
fun TimeOffScreen(
    cards: List<Inbox>,
    onBackClick: () -> Unit = {}
){
    var selectedStatus by remember { mutableStateOf("Pending") }

    val pendingCount = dummyTimeOff.count { it.CardStatus == "Pending" }
    val declineCount = dummyTimeOff.count { it.CardStatus == "Decline" }
    val approveCount = dummyTimeOff.count { it.CardStatus == "Approve" }

    val filteredItems = dummyTimeOff.filter { card ->
        when (selectedStatus) {
            "Approve" -> card.CardStatus == "Approve"
            "Pending" -> card.CardStatus == "Pending"
            "Decline" -> card.CardStatus == "Decline"
            else -> true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(top=20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.width(327.dp).padding(bottom = 16.dp),
        ){
            Text(text = stringResource(R.string.time_off_requirement), style = headline2)        }
        TabManageButton(
            onStatusSelected = { status -> selectedStatus = status },
            pendingCount = pendingCount,
            declineCount = declineCount,
            approveCount = approveCount)
        LazyColumn(
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredItems) { card ->
                CardInbox(
                    DateTime = card.DateTime,
                    Name = card.Name,
                    TypeLeave = card.TypeLeave,
                    CardImage = card.CardImage,
                    CardStatus = card.CardStatus,
                    onClick = card.onClick,
                    onDelete = card.onDelete,
                    availableDays = card.AvailableDays
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimeOffScreenPreview(){
    TimeOffScreen(
        cards = dummyTimeOff
    )
}