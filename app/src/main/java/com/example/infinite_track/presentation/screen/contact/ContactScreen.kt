package com.example.infinite_track.presentation.screen.contact

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.infinite_track.presentation.components.cards.ContactCard
import com.example.infinite_track.presentation.components.empty.EmptyListAnimation
import com.example.infinite_track.presentation.components.empty.ErrorAnimation
import com.example.infinite_track.presentation.components.loading.LoadingAnimation
import com.example.infinite_track.presentation.components.search.InfiniteTrackSearchBar
import com.example.infinite_track.presentation.core.headline2
import com.example.infinite_track.presentation.core.headline4

@Composable
fun ContactScreen(viewModel: ContactsViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val filteredContacts by viewModel.filteredContacts.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    var searchValue by remember { mutableStateOf("") }

    // Update search in ViewModel when searchValue changes
    LaunchedEffect(searchValue) {
        viewModel.onSearchQueryChanged(searchValue)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        topBar = {
            Row(
                modifier = Modifier
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Employees",
                    style = headline2,
                    fontWeight = FontWeight.Medium,
                    fontSize = 25.sp
                )
                Spacer(modifier = Modifier.padding(8.dp))
                Text(
                    text = filteredContacts.size.toString(),
                    style = headline2,
                    fontWeight = FontWeight.Medium,
                    fontSize = 25.sp
                )
            }
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Search Bar
                InfiniteTrackSearchBar(
                    modifier = Modifier.fillMaxWidth(),
                    value = searchValue,
                    placeholder = "Search Contacts",
                    onChange = { newValue -> searchValue = newValue }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Content area with loading/data/error states
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        isLoading -> {
                            LoadingAnimation()
                        }

                        errorMessage != null && errorMessage.toString().isNotEmpty() -> {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                ErrorAnimation(modifier = Modifier.size(150.dp))
                                Text(
                                    text = errorMessage.orEmpty(),
                                    style = headline4,
                                    color = Color.Red
                                )
                            }
                        }

                        filteredContacts.isEmpty() -> {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                EmptyListAnimation(modifier = Modifier.size(150.dp))
                                Text(
                                    text = "No contacts found matching \"$searchValue\"",
                                    style = headline4,
                                )
                            }
                        }

                        else -> {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(filteredContacts) { contact ->
                                    val fullImageUrl = if (!contact.photoUrl.isNullOrEmpty()) {
                                        contact.photoUrl
                                    } else {
                                        "https://w7.pngwing.com/pngs/177/551/png-transparent-user-interface-design-computer-icons-default-stephen-salazar-graphy-user-interface-design-computer-wallpaper-sphere-thumbnail.png"
                                    }

                                    ContactCard(
                                        name = contact.fullName,
                                        position = contact.division,
                                        cardImage = fullImageUrl,
                                        phone = contact.phoneNumber,
                                        message = contact.email,
                                        whatsapp = contact.phoneNumber, // Using phone for WhatsApp
                                        onClickCard = {},
                                        messageWA = "Halo ${contact.fullName}",
                                        context = context
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