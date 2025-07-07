package com.example.infinite_track.presentation.components.dropdown

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusFilterDropdown(
    selectedStatus: String?,
    onStatusSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    val statusOptions = mapOf(
        "all" to "Semua",
        "pending" to "Pending",
        "approved" to "Approved",
        "rejected" to "Rejected"
    )

    // Convert null selectedStatus to "all" for display
    val displayStatus = selectedStatus ?: "all"

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = statusOptions[displayStatus] ?: "Semua",
            onValueChange = {},
            readOnly = true,
            label = { Text("Filter Status") },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Arrow"
                )
            },
            modifier = Modifier
                .menuAnchor()
                .width(150.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            statusOptions.forEach { (value, label) ->
                DropdownMenuItem(
                    text = { Text(label) },
                    onClick = {
                        onStatusSelected(value)
                        expanded = false
                    }
                )
            }
        }
    }
}
