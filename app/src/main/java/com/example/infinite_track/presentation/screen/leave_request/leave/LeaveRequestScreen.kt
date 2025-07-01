package com.example.infinite_track.presentation.screen.leave_request.leave

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.infinite_track.presentation.components.button.InfiniteTracButtonBack
import com.example.infinite_track.presentation.core.headline3
import com.example.infinite_track.presentation.core.headline4
import com.example.infinite_track.presentation.theme.Blue_500


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaveRequestScreen(
    navigateToMyLeave: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    // Leave type dropdown state
    var expandedLeaveType by remember { mutableStateOf(false) }
    var selectedLeaveType by remember { mutableStateOf("Pilih Jenis Cuti") }

    // Form states
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    val leaveTypes = listOf(
        "Cuti Tahunan",
        "Cuti Sakit",
        "Cuti Melahirkan",
        "Cuti Penting",
        "Cuti Besar"
    )

    Scaffold(
        topBar = {
            InfiniteTracButtonBack(
                title = "Pengajuan Cuti",
                navigationBack = onBackClick,
                modifier = Modifier.padding(top = 12.dp)
            )
        },
        containerColor = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.height(60.dp))

                // Title
                Text(
                    text = "Form Pengajuan Cuti",
                    style = headline3.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Form Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // Leave Type Dropdown
                        Text(
                            text = "Jenis Cuti",
                            style = headline4.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        ExposedDropdownMenuBox(
                            expanded = expandedLeaveType,
                            onExpandedChange = { expandedLeaveType = it },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = selectedLeaveType,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedLeaveType) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                placeholder = { Text("Pilih Jenis Cuti") }
                            )

                            ExposedDropdownMenu(
                                expanded = expandedLeaveType,
                                onDismissRequest = { expandedLeaveType = false }
                            ) {
                                leaveTypes.forEach { leaveType ->
                                    DropdownMenuItem(
                                        text = { Text(leaveType) },
                                        onClick = {
                                            selectedLeaveType = leaveType
                                            expandedLeaveType = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Date Range
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Start Date
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 8.dp)
                            ) {
                                Text(
                                    text = "Tanggal Mulai",
                                    style = headline4.copy(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )

                                OutlinedTextField(
                                    value = startDate,
                                    onValueChange = { startDate = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    placeholder = { Text("DD/MM/YYYY") },
                                    trailingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.DateRange,
                                            contentDescription = "Select Date"
                                        )
                                    }
                                )
                            }

                            // End Date
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 8.dp)
                            ) {
                                Text(
                                    text = "Tanggal Selesai",
                                    style = headline4.copy(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )

                                OutlinedTextField(
                                    value = endDate,
                                    onValueChange = { endDate = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    placeholder = { Text("DD/MM/YYYY") },
                                    trailingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.DateRange,
                                            contentDescription = "Select Date"
                                        )
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Reason
                        Text(
                            text = "Alasan Cuti",
                            style = headline4.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        OutlinedTextField(
                            value = reason,
                            onValueChange = { reason = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Masukkan alasan cuti") },
                            minLines = 3
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Phone Number
                        Text(
                            text = "Nomor Telepon",
                            style = headline4.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        OutlinedTextField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Masukkan nomor telepon") }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Address
                        Text(
                            text = "Alamat Selama Cuti",
                            style = headline4.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        OutlinedTextField(
                            value = address,
                            onValueChange = { address = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Masukkan alamat") },
                            minLines = 2
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Submit Button
                        Button(
                            onClick = {
                                // Untuk saat ini hanya navigasi ke MyLeave tanpa logika
                                navigateToMyLeave()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Blue_500),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Ajukan Cuti",
                                modifier = Modifier.padding(vertical = 8.dp),
                                fontSize = 16.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
