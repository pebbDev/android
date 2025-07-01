//package com.example.infinite_track.presentation.screen.leave_request.leave
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.infinite_track.data.soucre.NetworkResponse
//import com.example.infinite_track.data.soucre.network.response.LeaveRequestResponse
//import com.example.infinite_track.data.soucre.repository.leave.leave_request.LeaveRequestRepository
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//import okhttp3.MultipartBody
//import okhttp3.RequestBody.Companion.toRequestBody
//import javax.inject.Inject
//
//@HiltViewModel
//class LeaveRequestViewModel @Inject constructor(
//    private val leaveRequestRepository: LeaveRequestRepository
//): ViewModel(){
//
//    private val _leaveRequestState = MutableStateFlow<NetworkResponse<LeaveRequestResponse>>(NetworkResponse.Idle)
//    val leaveRequestState: StateFlow<NetworkResponse<LeaveRequestResponse>> = _leaveRequestState
//
//    fun handleLeave(
//        name: String,
//        headProgramName: String,
//        division: String,
//        startDate: String,
//        endDate: String,
//        leaveType: String,
//        desc: String,
//        phone: String,
//        address: String,
//        uploadImage: MultipartBody.Part? = null
//    ) {
//        viewModelScope.launch {
//            _leaveRequestState.value = NetworkResponse.Loading
//
//            try {
//                handleLeaveRequest(
//                   name = name,
//                    headProgramName = headProgramName,
//                    division = division,
//                    startDate = startDate,
//                    endDate = endDate,
//                    leaveType = leaveType,
//                    desc = desc,
//                    phone = phone,
//                    address = address,
//                    uploadImage = uploadImage
//                )
//            } catch (e: Exception) {
//                _leaveRequestState.value = NetworkResponse.Error(e.message ?: "Unknown error")
//            }
//        }
//    }
//
//    private suspend fun handleLeaveRequest(
//        name: String,
//        headProgramName: String,
//        division: String,
//        startDate: String,
//        endDate: String,
//        leaveType: String,
//        desc: String,
//        phone: String,
//        address: String,
//        uploadImage: MultipartBody.Part?
//    ) {
//        if (uploadImage == null) {
//            _leaveRequestState.value = NetworkResponse.Error("Image is required for Leave Request!")
//            return
//        }
//
//        leaveRequestRepository.leaveRequest(
//            name.toRequestBody(),
//            headProgramName.toRequestBody(),
//            division.toRequestBody(),
//            startDate.toRequestBody(),
//            endDate.toRequestBody(),
//            leaveType.toRequestBody(),
//            desc.toRequestBody(),
//            phone.toRequestBody(),
//            address.toRequestBody(),
//            uploadImage!!
//        ).collect { response ->
//            _leaveRequestState.value = NetworkResponse.Success(response)
//        }
//    }
//}