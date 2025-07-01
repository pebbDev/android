//package com.example.infinite_track.presentation.screen.attendance
//
//import android.annotation.SuppressLint
//import android.net.Uri
//import android.widget.Toast
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.systemBarsPadding
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextFieldDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.zIndex
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.lifecycle.Lifecycle
//import androidx.lifecycle.compose.LocalLifecycleOwner
//import androidx.navigation.compose.rememberNavController
//import cn.pedant.SweetAlert.SweetAlertDialog
//import coil.compose.rememberAsyncImagePainter
//import com.example.infinite_track.R
//import com.example.infinite_track.data.soucre.NetworkResponse
//import com.example.infinite_track.data.soucre.network.response.AttendanceResponse
//import com.example.infinite_track.presentation.components.button.InfiniteTracButtonBack
//import com.example.infinite_track.presentation.components.button.TabButton
//import com.example.infinite_track.presentation.components.cameras.CameraCapture
//import com.example.infinite_track.presentation.components.loading.LoadingAnimation
//import com.example.infinite_track.presentation.components.tittle.AttendanceDate
//import com.example.infinite_track.presentation.components.tittle.LocalTimeText
//import com.example.infinite_track.presentation.core.body1
//import com.example.infinite_track.presentation.core.headline1
//import com.example.infinite_track.presentation.core.headline3
//import com.example.infinite_track.presentation.core.headline4
//import com.example.infinite_track.presentation.theme.Blue_500
//import com.example.infinite_track.presentation.theme.Purple_200
//import com.example.infinite_track.presentation.theme.Purple_300
//import com.example.infinite_track.utils.DialogHelper
//import com.example.infinite_track.utils.DialogHelper.showDialogWarning
//import com.example.infinite_track.utils.RequestLocationPermission
//import com.example.infinite_track.utils.getCurrentDate
//import com.example.infinite_track.utils.toMultipartBodyPart
//import kotlinx.coroutines.launch
//
//
//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AttendanceScreen(
//    attendanceViewModel: AttendanceViewModel = hiltViewModel(),
//    onBackClick: () -> Unit,
//) {
//    val context = LocalContext.current
//    val coroutineScope = rememberCoroutineScope()
//    val attendanceState by attendanceViewModel.attendanceState.collectAsState()
//    val checkInState by attendanceViewModel.checkInState.collectAsState()
//    val buttonLabel = when {
//        attendanceState.isCheckedOut -> "Have a nice day\uD83E\uDD70"
//        attendanceState.isAttend -> "Check-Out"
//        else -> "Check-In"
//    }
//
//    var notes by remember { mutableStateOf("") }
//
//    LaunchedEffect(Unit) {
//        attendanceViewModel.resetAttendanceState()
//    }
//
//    var showCamera by remember { mutableStateOf(false) }
//    var imageUri by remember { mutableStateOf<Uri?>(null) }
//
//    var showCamera2 by remember { mutableStateOf(false) }
//
//    var dialog by remember { mutableStateOf<SweetAlertDialog?>(null) }
//
//    var isImageUploaded by remember { mutableStateOf(false) }
//
//    val navController = rememberNavController()
//
//    var location by remember { mutableStateOf(Pair(0f, 0f)) }
//    val currentLocation by attendanceViewModel.currentLocation.collectAsState()
//
//    LaunchedEffect(currentLocation) {
//        currentLocation?.let {
//            location = Pair(it.first.toFloat(), it.second.toFloat())
//        }
//    }
//
//    RequestLocationPermission(
//        onPermissionGranted = {
//            attendanceViewModel.startLocationUpdates(context)
//        },
//        onPermissionDenied = {
//            Toast.makeText(context, "Location permission denied", Toast.LENGTH_SHORT).show()
//        }
//    )
//
//    var isWFH by remember { mutableStateOf(false) }
//    val launcher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.RequestPermission(),
//        onResult = { isGranted ->
//            if (isGranted) {
//                showCamera = true
//            } else {
//                Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
//            }
//        }
//    )
//
//    val launcher2 = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.RequestPermission(),
//        onResult = { isGranted ->
//            if (isGranted) {
//                showCamera2 = true
//            } else {
//                Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
//            }
//        }
//    )
//
//    Scaffold(
//        modifier = Modifier.fillMaxSize(),
//        containerColor = Color.Transparent,
//        topBar = {
//            InfiniteTracButtonBack(
//                title = "Live Attendance",
//                navigationBack = onBackClick,
//                modifier = Modifier.padding(top = 12.dp)
//            )
//        }
//    ){ innerPadding ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp)
//        ) {
//            Column(
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally,
//                modifier = Modifier
//                    .padding(innerPadding)
//                    .fillMaxSize()
//            ) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxHeight()
//                        .verticalScroll(rememberScrollState()),
//                ) {
//                    Column(
//                    ) {
//                        TabButton(
//                            onTabSelected = { selectedTab ->
//                                isWFH = selectedTab == "Work From Home"
//                            })
//                    }
//
//                    Spacer(modifier = Modifier.padding(2.dp))
//
//                    Column(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(vertical = 10.dp),
//                        verticalArrangement = Arrangement.Center,
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        Spacer(modifier = Modifier.padding(1.dp))
//                        LocalTimeText()
//                        AttendanceDate(date = getCurrentDate())
//                    }
//
//                    Spacer(modifier = Modifier.padding(5.dp))
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .background(
//                                color = Color(0x33FFFFFF),
//                                shape = RoundedCornerShape(10.dp)
//                            )
//                            .border(
//                                width = 1.dp,
//                                color = Color.White,
//                                shape = RoundedCornerShape(10.dp)
//                            ),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Column(
//                            modifier = Modifier.padding(20.dp),
//                            verticalArrangement = Arrangement.Center,
//                            horizontalAlignment = Alignment.CenterHorizontally
//                        ) {
//                            Text(
//                                text = "Normal",
//                                style = body1,
//                                color = Purple_300
//                            )
//                            Text(
//                                text = "09:00 AM - 17:00 PM",
//                                style = headline1,
//                            )
//                            Spacer(modifier = Modifier.padding(10.dp))
//                            Row(
//                                modifier = Modifier,
//                                verticalAlignment = Alignment.CenterVertically
//                            ) {
//                                Image(
//                                    painter = if (attendanceState.isAttend) {
//                                        painterResource(id = R.drawable.ic_checkout)
//                                    } else {
//                                        painterResource(id = R.drawable.ic_checkin)
//                                    },
//                                    contentDescription = "Check-In/Check-Out Icon",
//                                    modifier = Modifier.size(24.dp)
//                                )
//
//                                Spacer(modifier = Modifier.padding(5.dp))
//                                Text(
//                                    text = if (attendanceState.isCheckedOut) {
//                                        "Have a nice day🥰"
//                                    } else if (attendanceState.isAttend) {
//                                        "Check-Out"
//                                    } else {
//                                        "Check-In"
//                                    },
//                                    style = body1,
//                                    color = Purple_300
//                                )
//                            }
//                        }
//
//                    }
//
//                    Spacer(modifier = Modifier.padding(14.dp))
//
//                    Column(
//                        modifier = Modifier,
//                        verticalArrangement = Arrangement.Center,
//                    ) {
//                        Text(
//                            text = stringResource(R.string.write_notes),
//                            style = headline4
//                        )
//
//                        OutlinedTextField(
//                            value = notes,
//                            onValueChange = { notes = it },
//                            label = {
//                                Text(
//                                    text = stringResource(R.string.write_notes),
//                                    style = body1,
//                                    color = Purple_200
//                                )
//                            },
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(140.dp),
//                            colors = TextFieldDefaults.outlinedTextFieldColors(
//                                focusedBorderColor = Color.White,
//                                unfocusedBorderColor = Color.White,
//                                containerColor = Color(0X33FFFFFF)
//                            ),
//                            shape = RoundedCornerShape(10.dp),
//                        )
//                    }
//                    Spacer(modifier = Modifier.padding(8.dp))
//
//                    if (isWFH) {
//                        Column(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                        ) {
//                            Text(
//                                text = stringResource(R.string.upload_image),
//                                style = headline4
//                            )
//                        }
//
//                        Spacer(modifier = Modifier.padding(8.dp))
//
//                        // Camera Row
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                        ) {
//                            // Camera1
//                            if (!isImageUploaded) {
//                                Button(
//                                    onClick = {
//                                        launcher.launch(android.Manifest.permission.CAMERA)
//                                    },
//                                    colors = ButtonDefaults.buttonColors(Color.Transparent),
//                                    elevation = null,
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .border(
//                                            BorderStroke(2.dp, Color.White),
//                                            RoundedCornerShape(10.dp)
//                                        )
//                                        .background(Color(0x80F3ECFF), RoundedCornerShape(10.dp))
//                                        .size(175.dp),
//                                    shape = RoundedCornerShape(10.dp)
//                                ) {
//                                    Column(
//                                        modifier = Modifier
//                                            .fillMaxWidth()
//                                            .padding(top = 15.dp),
//                                        verticalArrangement = Arrangement.Center,
//                                        horizontalAlignment = Alignment.CenterHorizontally
//
//                                    ) {
//                                        Image(
//                                            painter = painterResource(id = R.drawable.cameras2),
//                                            contentDescription = "Camera Icon",
//                                            modifier = Modifier.size(50.dp),
//                                            contentScale = ContentScale.Fit,
//                                        )
//                                        Spacer(modifier = Modifier.padding(7.dp))
//
//                                        Text(
//                                            text = stringResource(R.string.a_photo_of_you),
//                                            style = headline3,
//                                            color = Color.Black
//                                        )
//
//                                        Spacer(modifier = Modifier.padding(5.dp))
//
//                                        Text(
//                                            text = stringResource(R.string.photo_face_instruction),
//                                            style = body1,
//                                            color = Purple_300
//                                        )
//                                    }
//                                }
//                            }
//                            imageUri?.let { uri ->
//                                Spacer(modifier = Modifier.height(16.dp))
//                                Image(
//                                    painter = rememberAsyncImagePainter(model = uri),
//                                    contentDescription = "Captured Image",
//                                    modifier = Modifier.size(200.dp)
//                                )
//                            }
//                        }
//                    }
//
//                    Spacer(modifier = Modifier.padding(12.dp))
//
//                    Column(
//                        modifier = Modifier
//                            .fillMaxSize()
////                        .padding(16.dp)
//                    ) {
//                        Spacer(modifier = Modifier.weight(1f))
////                    Text("Location: ${location.first}, ${location.second}")
//                        Button(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(50.dp)
//                                .padding(horizontal = if (buttonLabel == "Have a nice day\uD83E\uDD70") 0.dp else 0.dp),
//                            shape = RoundedCornerShape(if (buttonLabel == "Have a nice day\uD83E\uDD70") 20.dp else 10.dp),
//                            colors = if (buttonLabel == "Have a nice day\uD83E\uDD70") {
//                                ButtonDefaults.outlinedButtonColors(
//                                    containerColor = Color.Transparent,
//                                    contentColor = Blue_500
//                                )
//                            } else {
//                                ButtonDefaults.buttonColors(
//                                    containerColor = Blue_500,
//                                    contentColor = Color.White
//                                )
//                            },
//                            border = if (buttonLabel == "Have a nice day\uD83E\uDD70") {
//                                BorderStroke(1.dp, Blue_500)
//                            } else {
//                                null
//                            },
//                            onClick = {
//                                coroutineScope.launch {
//                                    if (attendanceState.isCheckedOut) {
//                                        dialog?.dismissWithAnimation()
//                                        dialog = DialogHelper.showDialogSuccess(
//                                            context = context,
//                                            title = "You have checked out today",
//                                            textContent = "Have a nice day\uD83E\uDD70",
//                                            imageRes = R.drawable.img_succes,
//                                            onConfirm = { dialog?.dismissWithAnimation() },
//                                        )
//                                    } else {
//                                        if (attendanceState.isAttend) {
//                                            dialog?.dismissWithAnimation()
//                                            dialog = showDialogWarning(
//                                                context = context,
//                                                title = "Are you sure you want to Check-Out?",
//                                                textContent = "You are about to check out. Please confirm.",
//                                                onConfirm = {
//                                                    // Proceed with Check-Out
//                                                    val action = "checkout"
//                                                    val attendanceCategory =
//                                                        if (isWFH) "Work From Home" else "Work From Office"
//                                                    attendanceViewModel.handleAttendance(
//                                                        attendanceCategory = attendanceCategory,
//                                                        latitude = location.first,
//                                                        longitude = location.second,
//                                                        action = action,
//                                                        notes = notes,
//                                                        uploadImage = if (isWFH) {
//                                                            imageUri?.toMultipartBodyPart(
//                                                                "upload_image",
//                                                                context
//                                                            )
//                                                        } else null
//                                                    )
//                                                    onBackClick()
//                                                },
//                                                onDismis = { dialog?.dismissWithAnimation() }
//                                            )
//                                        } else {
//                                            // Proceed with Check-In
//                                            val action = "checkin"
//                                            val attendanceCategory =
//                                                if (isWFH) "Work From Home" else "Work From Office"
//                                            attendanceViewModel.handleAttendance(
//                                                attendanceCategory = attendanceCategory,
//                                                latitude = location.first,
//                                                longitude = location.second,
//                                                action = action,
//                                                notes = notes,
//                                                uploadImage = if (isWFH) {
//                                                    imageUri?.toMultipartBodyPart(
//                                                        "upload_image",
//                                                        context
//                                                    )
//                                                } else null
//                                            )
//                                        }
//                                    }
//                                }
//                            },
//                            enabled = checkInState !is NetworkResponse.Loading
//                        ) {
//                            Text(text = buttonLabel)
//                        }
//                    }
//                }
//            }
//        }
//
//        val lifecycleOwner = LocalLifecycleOwner.current
//        val currentScreen = lifecycleOwner.lifecycle.currentState
//
//        Box(modifier = Modifier.fillMaxSize()) {
//            if (currentScreen == Lifecycle.State.RESUMED) {
//                when (checkInState) {
//                    is NetworkResponse.Loading -> {
//                        Box(
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .background(Color.Black.copy(alpha = 0.5f))
//                                .zIndex(1f),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            LoadingAnimation()
//                        }
//                    }
//
//                    is NetworkResponse.Success -> {
//                        val message =
//                            (checkInState as NetworkResponse.Success<AttendanceResponse>).data.message
//                        LaunchedEffect(Unit) {
//                            DialogHelper.showDialogSuccess(
//                                context = context,
//                                title = "Attendance Status",
//                                textContent = message,
//                                imageRes = R.drawable.img_login,
//                                onConfirm = {
//                                    attendanceViewModel.resetCheckInState()
//                                    onBackClick()
//                                }
//                            )
//                        }
//                    }
//
//                    is NetworkResponse.Error -> {
//                        val errorMessage = (checkInState as NetworkResponse.Error).message
//                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
//                    }
//
//                    else -> {}
//                }
//            }
//        }
//
//        if (showCamera) {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(Color.Black.copy(alpha = 0.8f))
//                    .zIndex(1f)
//                    .systemBarsPadding()
//            ) {
//                CameraCapture(
//                    modifier = Modifier.fillMaxSize(),
//                    onImageCaptured = { uri ->
//                        if (uri != null) {
//                            imageUri = uri
//                            isImageUploaded = true
//                        }
//                        showCamera = false
//                    },
//                    onError = { /* Error Handling */ }
//                )
//            }
//        }
//    }
//}
//
