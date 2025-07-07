package com.example.infinite_track.presentation.screen.booking

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.infinite_track.presentation.components.button.InfiniteTracButtonBack
import com.example.infinite_track.presentation.components.cards.BookingHistoryCard
import com.example.infinite_track.presentation.components.dropdown.StatusFilterComponent
import com.example.infinite_track.presentation.components.empty.EmptyListAnimation
import com.example.infinite_track.presentation.components.loading.LoadingAnimation
import com.example.infinite_track.presentation.core.headline4
import com.example.infinite_track.presentation.screen.home.HomeViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsMyBooking(
    viewModel: HomeViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.bookingHistoryDetailsState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    // Initialize data when screen is first loaded - FIX: Force refresh
    LaunchedEffect(Unit) {
        // Reset state first to ensure clean load
        viewModel.onBookingStatusFilterChanged("all")
    }

    // Infinite scroll logic
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleItemIndex ->
                if (lastVisibleItemIndex != null &&
                    lastVisibleItemIndex >= uiState.bookings.size - 3 &&
                    uiState.canLoadMore &&
                    !uiState.isLoading
                ) {
                    viewModel.loadMoreBookings()
                }
            }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        topBar = {
            InfiniteTracButtonBack(
                title = "Riwayat Booking",
                navigationBack = onBackClick,
                modifier = Modifier.padding(top = 24.dp) // Add proper status bar spacing
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Header with filter controls - Updated style
            Text(
                text = "Riwayat Booking WFA",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Filter with TodayDateWithFilterHeader style
            StatusFilterComponent(
                selectedStatus = uiState.selectedStatus,
                onStatusSelected = { newStatus ->
                    viewModel.onBookingStatusFilterChanged(newStatus)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Content - Fixed the loading condition
            when {
                uiState.isLoading && uiState.bookings.isEmpty() -> {
                    // Initial loading state
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingAnimation()
                    }
                }

                uiState.error != null && uiState.bookings.isEmpty() -> {
                    // Error state
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            EmptyListAnimation(modifier = Modifier.size(150.dp))
                            Spacer(modifier = Modifier.height(16.dp))
                            val errorMessage = uiState.error ?: "Unknown error"
                            Text(
                                text = errorMessage,
                                style = headline4,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }

                uiState.bookings.isEmpty() && !uiState.isLoading -> {
                    // Empty state - only show when not loading
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            EmptyListAnimation(modifier = Modifier.size(150.dp))
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Tidak ada riwayat booking",
                                style = headline4
                            )
                        }
                    }
                }

                else -> {
                    // Success state with data
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        itemsIndexed(uiState.bookings) { index, booking ->
                            BookingHistoryCard(
                                booking = booking,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        // Loading indicator for pagination
                        if (uiState.isLoading && uiState.bookings.isNotEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }

                        // End of list indicator
                        if (!uiState.canLoadMore && uiState.bookings.isNotEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Tidak ada data lagi",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
