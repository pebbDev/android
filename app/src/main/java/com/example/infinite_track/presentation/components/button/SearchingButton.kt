package com.example.infinite_track.presentation.components.button

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.infinite_track.R
import com.example.infinite_track.presentation.core.headline4
import com.example.infinite_track.presentation.theme.Blue_500
import com.example.infinite_track.presentation.theme.Purple_200
import com.example.infinite_track.presentation.theme.Purple_300
import com.example.infinite_track.presentation.theme.Purple_400

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchingButton(
    modifier: Modifier = Modifier,
    onCalendarClick: () -> Unit,
    searchQuery: String,
    onQueryChanged: (String) -> Unit,
) {
    Box(
        modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically

        ) {
            Icon(
                painter = painterResource(R.drawable.ic_searching),
                contentDescription = "Search Icon",
                tint = Purple_400,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            TextField(
                value = searchQuery,
                onValueChange = onQueryChanged,
                textStyle = TextStyle(
                    color = Purple_300,
                    fontSize = 16.sp
                ),
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Transparent, shape = RoundedCornerShape(8.dp))
                    .indicatorLine(
                        enabled = false,
                        isError = false,
                        interactionSource = remember {
                            MutableInteractionSource()
                        },
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                        ),
                        focusedIndicatorLineThickness = 0.dp,
                        unfocusedIndicatorLineThickness = 0.dp
                    ),
                placeholder = {
                    Text(
                        text = stringResource(R.string.search_hint),
                        color = Purple_200,
                        style = headline4
                    )
                },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedContainerColor = Color.White,
                    unfocusedTextColor = Color.Black,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = Blue_500,
                )
            )

            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                painter = painterResource(R.drawable.ic_calendar),
                contentDescription = "Calendar Icon",
                tint = Blue_500,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onCalendarClick() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchingButtonPreview() {
    var searchQuery by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xFFF0E5FF))
            .padding(16.dp)
    ) {
        SearchingButton(
            searchQuery = searchQuery,
            onQueryChanged = { newQuery -> searchQuery = newQuery },
            onCalendarClick = { /* TODO: Handle Calendar Click */ }
        )
    }
}

