package com.example.infinite_track.presentation.screen.profile.details.edit_profile

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.infinite_track.presentation.components.avatar.ProfileCard
import com.example.infinite_track.presentation.components.button.CancelButton
import com.example.infinite_track.presentation.components.button.InfiniteTracButtonBack
import com.example.infinite_track.presentation.components.button.InfiniteTrackButton
import com.example.infinite_track.presentation.components.profile_textfield.PhoneNumberTextFieldComponent
import com.example.infinite_track.presentation.components.profile_textfield.ProfileTextFieldComponent
import com.example.infinite_track.utils.UiState

/**
 * Edit Profile Screen component
 * Purely reactive UI that only displays state from ViewModel
 * and forwards user actions to ViewModel
 */
@Composable
fun EditProfile(
    onBackClick: () -> Unit,
    viewModel: EditProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    // Collect states from ViewModel
    val userProfile by viewModel.userProfileState.collectAsState()
    val fullName by viewModel.fullName.collectAsState()
    val phone by viewModel.phone.collectAsState()
    val nipNim by viewModel.nipNim.collectAsState()
    val isEditing by viewModel.isEditing.collectAsState()
    val updateProfileState by viewModel.updateProfileState.collectAsState()

    // Process update profile state
    LaunchedEffect(updateProfileState) {
        when (updateProfileState) {
            is UiState.Success -> {
                Toast.makeText(
                    context,
                    "Profile updated successfully!",
                    Toast.LENGTH_SHORT
                ).show()
                onBackClick() // Navigate back on successful update
                viewModel.resetUpdateState() // Reset state after handling
            }

            is UiState.Error -> {
                Toast.makeText(
                    context,
                    (updateProfileState as UiState.Error).errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.resetUpdateState() // Reset state after handling error
            }

            else -> { /* Do nothing for other states */
            }
        }
    }

    // UI Layout
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        topBar = {
            InfiniteTracButtonBack(
                title = "Edit Profile",
                navigationBack = onBackClick,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Loading indicator when update is in progress
            if (updateProfileState is UiState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Only show profile card if user profile is available
                userProfile?.let { user ->
                    ProfileCard(
                        imageResId = com.example.infinite_track.R.drawable.logo,
                        name = user.fullName,
                        jobTitle = user.positionName ?: "No Position"
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Editable fields
                ProfileTextFieldComponent(
                    label = "Full Name",
                    value = fullName,
                    onValueChange = { viewModel.onFullNameChange(it) },
                    enabled = isEditing
                )

                Spacer(modifier = Modifier.height(8.dp))

                ProfileTextFieldComponent(
                    label = "NIP / NIM",
                    value = nipNim,
                    onValueChange = { viewModel.onNipNimChange(it) },
                    enabled = isEditing
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Read-only fields
                userProfile?.let { user ->
                    ProfileTextFieldComponent(
                        label = "Division",
                        value = user.divisionName ?: "No Division",
                        enabled = false
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ProfileTextFieldComponent(
                        label = "Position",
                        value = user.positionName ?: "No Position",
                        enabled = false
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ProfileTextFieldComponent(
                        label = "Email",
                        value = user.email,
                        enabled = false
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                PhoneNumberTextFieldComponent(
                    label = "Phone Number",
                    value = phone,
                    onValueChange = { viewModel.onPhoneChange(it) },
                    enabled = isEditing
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    if (isEditing) {
                        InfiniteTrackButton(
                            label = "Save",
                            onClick = { viewModel.onSaveChangesClick() },
                            enabled = true,
                            isOutline = false
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        CancelButton(
                            onClick = { viewModel.onCancelClick() },
                            label = "Cancel"
                        )
                    }

                    if (!isEditing) {
                        InfiniteTrackButton(
                            label = "Edit",
                            onClick = { viewModel.onToggleEditMode() },
                            enabled = true,
                            isOutline = false
                        )
                    }
                }
            }
        }
    }
}
