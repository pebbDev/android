//package com.example.infinite_track.presentation.screen.attendance
//
//import android.content.Context
//import android.content.pm.PackageManager
//import android.os.Looper
//import android.util.Log
//import androidx.core.app.ActivityCompat
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.infinite_track.data.soucre.NetworkResponse
//import com.example.infinite_track.data.soucre.network.request.AttendanceRequest
//import com.example.infinite_track.data.soucre.network.response.AttendanceResponse
//import com.example.infinite_track.domain.model.AttendanceState
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationCallback
//import com.google.android.gms.location.LocationRequest
//import com.google.android.gms.location.LocationResult
//import com.google.android.gms.location.Priority
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.launchIn
//import kotlinx.coroutines.flow.onEach
//import kotlinx.coroutines.flow.stateIn
//import kotlinx.coroutines.flow.take
//import kotlinx.coroutines.launch
//import okhttp3.MediaType.Companion.toMediaTypeOrNull
//import okhttp3.MultipartBody
//import okhttp3.RequestBody.Companion.toRequestBody
//import javax.inject.Inject
//
//@HiltViewModel
//class AttendanceViewModel @Inject constructor(
//    private val attendanceRepository: AttendanceRepository,
//    private val fusedLocationClient: FusedLocationProviderClient
//
//) : ViewModel() {
//
//    init {
//        viewModelScope.launch {
//            attendanceRepository.resetAttendanceIfNeeded()
//        }
//    }
//
//    private val _currentLocation = MutableStateFlow<Pair<Double, Double>?>(null)
//    val currentLocation: StateFlow<Pair<Double, Double>?> = _currentLocation
//
//    fun startLocationUpdates(context: Context) {
//        val locationRequest = LocationRequest.Builder(
//            Priority.PRIORITY_HIGH_ACCURACY,
//            10000
//        ).setMinUpdateIntervalMillis(5000)
//            .build()
//
//        if (ActivityCompat.checkSelfPermission(
//                context,
//                android.Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            return
//        }
//
//        fusedLocationClient.requestLocationUpdates(
//            locationRequest,
//            object : LocationCallback() {
//                override fun onLocationResult(locationResult: LocationResult) {
//                    val location = locationResult.lastLocation
//                    if (location != null) {
//                        _currentLocation.value = Pair(location.latitude, location.longitude)
//                        Log.d(
//                            "LocationUpdate",
//                            "Latitude: ${location.latitude}, Longitude: ${location.longitude}"
//                        )
//                    } else {
//                        Log.e("LocationUpdateError", "Failed to get location update")
//                    }
//                }
//            },
//            Looper.getMainLooper()
//        )
//    }
//
//    private suspend fun updateCheckInState(response: NetworkResponse<AttendanceResponse>) {
//        if (_checkInState.value != response) {
//            _checkInState.value = response
//        }
//    }
//
//    private val _checkInState =
//        MutableStateFlow<NetworkResponse<AttendanceResponse>>(NetworkResponse.Idle)
//    val checkInState: StateFlow<NetworkResponse<AttendanceResponse>> = _checkInState
//
//    val attendanceState = attendanceRepository.getAttendanceState()
//        .stateIn(
//            viewModelScope,
//            SharingStarted.WhileSubscribed(5000),
//            AttendanceState(false, false, 0L)
//        )
//
//    fun handleAttendance(
//        attendanceCategory: String,
//        latitude: Float,
//        longitude: Float,
//        action: String,
//        notes: String,
//        uploadImage: MultipartBody.Part? = null
//    ) {
//        viewModelScope.launch {
//            _checkInState.value = NetworkResponse.Loading
//
//            try {
//                val request = AttendanceRequest(
//                    attendanceCategory = attendanceCategory,
//                    latitude = latitude,
//                    longitude = longitude,
//                    action = action,
//                    notes = notes
//                )
//
//                if (attendanceCategory == "Work From Office") {
//                    if (!validateGeofence(latitude, longitude)) {
//                        _checkInState.value = NetworkResponse.Error("Please move to the designated location!")
//                        return@launch
//                    }
//
//                    attendanceRepository.workFromOffice(request).take(1).collect { response ->
//                        updateAttendanceState(action)
//                        updateCheckInState(NetworkResponse.Success(response))
//                    }
//                } else {
//                    if (uploadImage == null && action == "checkin") {
//                        _checkInState.value = NetworkResponse.Error("Image is required for Work From Home!")
//                        return@launch
//                    }
//
//                    attendanceRepository.workFromHome(
//                        attendanceCategory.toRequestBody(),
//                        action.toRequestBody(),
//                        notes.toRequestBody(),
//                        latitude.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
//                        longitude.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
//                        uploadImage!!
//                    ).take(1).collect { response ->
//                        updateAttendanceState(action)
//                        updateCheckInState(NetworkResponse.Success(response))
//                    }
//                }
//            } catch (e: Exception) {
//                _checkInState.value = NetworkResponse.Error(e.message ?: "Unknown error")
//            }
//        }
//    }
//
//    private suspend fun handleWorkFromOffice(
//        attendanceCategory: String,
//        latitude: Float,
//        longitude: Float,
//        action: String,
//        notes: String
//    ) {
//        if (!validateGeofence(latitude, longitude)) {
//            _checkInState.value = NetworkResponse.Error("Please move to the designated location!")
//            return
//        }
//
//        val request = AttendanceRequest(
//            attendanceCategory = attendanceCategory,
//            latitude = latitude,
//            longitude = longitude,
//            action = action,
//            notes = notes
//        )
//
//        attendanceRepository.workFromOffice(request)
//            .take(1) // Ensure only one emission
//            .onEach { response ->
//                updateAttendanceState(action)
//                if (_checkInState.value != NetworkResponse.Success(response)) {
//                    _checkInState.value = NetworkResponse.Success(response)
//                }
//            }
//            .launchIn(viewModelScope)
//    }
//
//    private suspend fun handleWorkFromHome(
//        attendanceCategory: String,
//        latitude: Float,
//        longitude: Float,
//        action: String,
//        notes: String,
//        uploadImage: MultipartBody.Part?
//    ) {
//
//        val latitudeBody = latitude.toString().toRequestBody("text/plain".toMediaTypeOrNull())
//        val longitudeBody = longitude.toString().toRequestBody("text/plain".toMediaTypeOrNull())
//
//        if (uploadImage == null && action == "checkin") {
//            _checkInState.value = NetworkResponse.Error("Image is required for Work From Home!")
//            return
//        }
//
//        Log.d("AttendanceRequest", "Latitude: $latitude, Longitude: $longitude, Action: $action")
//
//        attendanceRepository.workFromHome(
//            attendanceCategory.toRequestBody(),
//            action.toRequestBody(),
//            notes.toRequestBody(),
//            latitudeBody,
//            longitudeBody,
//            uploadImage!!
//        )
//            .take(1)
//            .onEach { response ->
//                updateAttendanceState(action)
//                _checkInState.value = NetworkResponse.Success(response)
//            }
//            .launchIn(viewModelScope)
//    }
//
//    private suspend fun updateAttendanceState(action: String) {
//        attendanceRepository.saveAttendanceState(
//            isAttend = action == "checkin",
//            isCheckedOut = action == "checkout",
//            lastCheckoutTime = System.currentTimeMillis()
//        )
//    }
//
//    fun validateGeofence(latitude: Float, longitude: Float): Boolean {
//        val officeLatitude = 1.1851f
//        val officeLongitude = 104.1019f
//        val allowedRadius = 500f
//
//        val distance = calculateDistance(
//            officeLatitude,
//            officeLongitude,
//            latitude,
//            longitude
//        )
//
//        println("Distance from office: $distance meters, Allowed radius: $allowedRadius meters")
//
//        return if (distance <= allowedRadius) {
//            true
//        } else {
//            println("Outside geofence. Distance: $distance meters exceeds radius: $allowedRadius meters.")
//            false
//        }
//    }
//
//    fun calculateDistance(lat1: Float, lon1: Float, lat2: Float, lon2: Float): Float {
//        val earthRadius = 6371.0 // in kilometers
//        val dLat = Math.toRadians(lat2.toDouble() - lat1.toDouble())
//        val dLon = Math.toRadians(lon2.toDouble() - lon1.toDouble())
//        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
//                Math.cos(Math.toRadians(lat1.toDouble())) * Math.cos(Math.toRadians(lat2.toDouble())) *
//                Math.sin(dLon / 2) * Math.sin(dLon / 2)
//        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
//
//        return (earthRadius * c * 1000).toFloat()
//    }
//
//    fun resetAttendanceState() {
//        viewModelScope.launch {
//            attendanceRepository.resetAttendanceIfNeeded()
//        }
//    }
//    fun resetCheckInState() {
//        _checkInState.value = NetworkResponse.Idle // Pastikan ada state "Idle" untuk menunggu request baru
//    }
//}
