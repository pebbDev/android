package com.example.infinite_track.presentation.components.dropdown

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortingDropdown(
    selectedSortBy: String,
    selectedSortOrder: String,
    onSortingChanged: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    val sortOptions = mapOf(
        "created_at" to "Tanggal Dibuat",
        "schedule_date" to "Tanggal Jadwal",
        "processed_at" to "Tanggal Diproses",
        "status" to "Status"
    )

    val sortOrderOptions = mapOf(
        "DESC" to "Terbaru",
        "ASC" to "Terlama"
    )

    // Create display text combining sort field and order
    val displayText = "${sortOptions[selectedSortBy]} (${sortOrderOptions[selectedSortOrder]})"

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = displayText,
            onValueChange = {},
            readOnly = true,
            label = { Text("Urutkan") },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Arrow"
                )
            },
            modifier = Modifier
                .menuAnchor()
                .width(200.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            sortOptions.forEach { (sortBy, sortByLabel) ->
                // Add both ASC and DESC options for each sort field
                sortOrderOptions.forEach { (order, orderLabel) ->
                    DropdownMenuItem(
                        text = { Text("$sortByLabel ($orderLabel)") },
                        onClick = {
                            onSortingChanged(sortBy, order)
                            expanded = false
                        }
                    )
                }

                // Add divider between different sort fields
                if (sortBy != sortOptions.keys.last()) {
                    Divider()
                }
            }
        }
    }
}
