//package com.example.infinite_track.presentation.screen.home.details
//
//import androidx.compose.foundation.ExperimentalFoundationApi
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
//import com.example.infinite_track.R
//import com.example.infinite_track.presentation.components.base.StaticBaseLayout
//import com.example.infinite_track.presentation.components.button.InfiniteTracButtonBack
//import com.example.infinite_track.presentation.components.button.SearchingButton
//import com.example.infinite_track.presentation.components.calendar.DateRangePickerModal
//import com.example.infinite_track.presentation.components.cards.TimeOffCard
//import com.example.infinite_track.presentation.components.empty.EmptyListAnimation
//import com.example.infinite_track.presentation.components.loading.LoadingAnimation
//import com.example.infinite_track.presentation.core.body1
//import com.example.infinite_track.utils.UiState
//import com.example.infinite_track.utils.toFormattedDate
//import java.util.Date
//
//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun DetailsTimeOffRequest(
//    detailsTimeOffRequest: DetailsViewModel = hiltViewModel(),
//    onBackClick: () -> Unit,
//) {
//    var showDatePicker by remember { mutableStateOf(false) }
//    val selectedDateRange by detailsTimeOffRequest.selectedDateRange.collectAsState()
//    val searchQuery by detailsTimeOffRequest.searchQuery.collectAsState()
//    val uiState by detailsTimeOffRequest.uiState.collectAsState()
//    val emptySearchMessage by detailsTimeOffRequest.emptySearchMessage.collectAsState()
//    val emptyDateRangeMessage by detailsTimeOffRequest.emptyDateRangeMessage.collectAsState()
//    val filteredList by detailsTimeOffRequest.filteredTimeOffList.collectAsState()
//
//    val (start, end) = selectedDateRange
//    val formattedStart = start?.toFormattedDate() ?: "N/A"
//    val formattedEnd = end?.toFormattedDate() ?: "N/A"
//
//    Scaffold(
//        modifier = Modifier.fillMaxSize(),
//        containerColor = Color.Transparent,
//        topBar = {
//            InfiniteTracButtonBack(
//                title = stringResource(R.string.time_off_request),
//                navigationBack = onBackClick,
//                modifier = Modifier.padding(top = 12.dp)
//            )
//        }
//    ) { innerPadding ->
//
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp)
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(innerPadding)
//            ) {
//                SearchingButton(
//                    searchQuery = searchQuery,
//                    onQueryChanged = detailsTimeOffRequest::onSearchQueryChanged,
//                    onCalendarClick = { showDatePicker = true },
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                when (uiState) {
//                    is UiState.Loading -> {
//                        Box(
//                            modifier = Modifier.fillMaxSize(),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            LoadingAnimation()
//                        }
//                    }
//                    is UiState.Success -> {
//                        if (filteredList.isEmpty()) {
//                            if (emptySearchMessage.isNotEmpty()) {
//                                Box(
//                                    modifier = Modifier.fillMaxSize(),
//                                    contentAlignment = Alignment.Center
//                                ) {
//                                    Column(
//                                        horizontalAlignment = Alignment.CenterHorizontally,
//                                    ) {
//                                        EmptyListAnimation(modifier = Modifier.size(150.dp))
//                                        Text(
//                                            text = emptySearchMessage,
//                                            style = body1
//                                        )
//                                    }
//                                }
//                            } else if (emptyDateRangeMessage.isNotEmpty()) {
//                                // Show empty date range message
//                                Box(
//                                    modifier = Modifier.fillMaxSize(),
//                                    contentAlignment = Alignment.Center
//                                ) {
//                                    Column(
//                                        horizontalAlignment = Alignment.CenterHorizontally,
//                                    ) {
//                                        EmptyListAnimation(modifier = Modifier.size(150.dp))
//                                        Text(
//                                            text = emptyDateRangeMessage,
//                                            style = body1
//                                        )
//                                    }
//                                }
//                            }
//                        } else {
//                            LazyColumn(
//                                modifier = Modifier.fillMaxSize()
//                            ) {
//                                items(filteredList) { timeOff ->
//                                    TimeOffCard(timeOff)
//                                }
//                            }
//                        }
//                    }
//                    is UiState.Error -> {
//                        Box(
//                            modifier = Modifier.fillMaxSize(),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Text(
//                                text = (uiState as UiState.Error).errorMessage,
//                                style = body1
//                            )
//                        }
//                    }
//                    is UiState.Idle -> {
//
//                    }
//                }
//
//
//                if (showDatePicker) {
//                    DateRangePickerModal(
//                        onDateRangeSelected = { range ->
//                            val (start, end) = range
//                            val startDate = start?.let { Date(it) }
//                            val endDate = end?.let { Date(it) }
//                            detailsTimeOffRequest.onDateRangeChanged(startDate to endDate)
//                            showDatePicker = false
//                        },
//                        onDismiss = { showDatePicker = false }
//                    )
//                }
//            }
//        }
//    }
//}
//
//
