package com.example.infinite_track.presentation.components.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.infinite_track.R
import com.example.infinite_track.presentation.core.body1
import com.example.infinite_track.presentation.theme.Purple_300
import com.example.infinite_track.presentation.theme.Purple_400
import com.example.infinite_track.presentation.theme.Purple_500
import com.example.infinite_track.presentation.theme.Violet_50

@Composable
fun InfiniteTrackDropDown(
    modifier: Modifier = Modifier,
    onSelected: (String) -> Unit,
    items: List<String>,
    placeholder: String = "",
    leadingIcon: Painter
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }

    var selectedItem by remember { mutableStateOf(placeholder) }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Violet_50.copy(alpha = 0.5f), shape = RoundedCornerShape(12.dp))
            .border(width = 1.dp, color = Violet_50, shape = RoundedCornerShape(12.dp))
            .padding(14.dp)
            .clickable {
                isExpanded = !isExpanded
            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = leadingIcon,
                    tint = Purple_500,
                    contentDescription = ""

                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = placeholder,
                    style = body1, color = Purple_400
                )
            }
            Icon(
                painter = painterResource(id = if (isExpanded) R.drawable.arrow_up else R.drawable.arrow_down),
                tint = Purple_300,
                contentDescription = ""
            )
        }
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item) },
                    onClick = {
                        selectedItem = item
                        isExpanded = false
                        onSelected(selectedItem)
                    }
                )
            }
        }
    }
}


@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun InfiniteTrackDropDownPreview() {
    Box(
        modifier = Modifier
            .background(Color.LightGray)
    ) {
        InfiniteTrackDropDown(
            onSelected = {},
            placeholder = "Pilih Head Program",
            leadingIcon = painterResource(id = R.drawable.ic_headprogram),
            items = listOf("items", "item")
        )
    }
}