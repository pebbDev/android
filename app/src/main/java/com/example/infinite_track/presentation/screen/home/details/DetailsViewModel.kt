//package com.example.infinite_track.presentation.screen.home.details
//
//import android.annotation.SuppressLint
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.infinite_track.data.soucre.network.response.DataLeave
//import com.example.infinite_track.data.soucre.repository.leave.LeaveRepository
//import com.example.infinite_track.domain.model.TimeOffModel
//import com.example.infinite_track.utils.UiState
//import com.example.infinite_track.utils.toFormattedDate
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//import java.text.SimpleDateFormat
//import java.util.Date
//import java.util.Locale
//import javax.inject.Inject
//@HiltViewModel
//class DetailsViewModel @Inject constructor(
//    private val leaveRepository: LeaveRepository
//) : ViewModel() {
//
//    private val _uiState = MutableStateFlow<UiState<List<TimeOffModel>>>(UiState.Loading)
//    val uiState: StateFlow<UiState<List<TimeOffModel>>> = _uiState
//
//    private val _allData = MutableStateFlow<List<DataLeave>>(emptyList())
//    private val _filteredTimeOffList = MutableStateFlow<List<TimeOffModel>>(emptyList())
//    val filteredTimeOffList: StateFlow<List<TimeOffModel>> = _filteredTimeOffList
//
//    private val _selectedDateRange = MutableStateFlow<Pair<Date?, Date?>>(null to null)
//    val selectedDateRange: StateFlow<Pair<Date?, Date?>> = _selectedDateRange
//
//    private val _searchQuery = MutableStateFlow("")
//    val searchQuery: StateFlow<String> = _searchQuery
//
//    private val _emptySearchMessage = MutableStateFlow("")
//    val emptySearchMessage: StateFlow<String> = _emptySearchMessage
//
//    private val _emptyDateRangeMessage = MutableStateFlow("")
//    val emptyDateRangeMessage: StateFlow<String> = _emptyDateRangeMessage
//
//    private val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
//
//    init {
//        getTimeOffData()
//    }
//
//    fun getTimeOffData() {
//        viewModelScope.launch {
//            _uiState.value = UiState.Loading
//            delay(1000)
//            leaveRepository.getAllLeave()
//                .collect { response ->
//                    val allData = response.dataLeave?.filterNotNull() ?: emptyList()
//                    _allData.value = allData
//                    applyFilters()
//                    _uiState.value = UiState.Success(_filteredTimeOffList.value)
//                }
//        }
//    }
//
//    fun onSearchQueryChanged(query: String) {
//        _searchQuery.value = query
//        searchByNameOrDivision(query)
//    }
//
//    fun onDateRangeChanged(range: Pair<Date?, Date?>) {
//        _selectedDateRange.value = range
//        searchByDateRange(range)
//    }
//
//    private fun searchByNameOrDivision(query: String) {
//        viewModelScope.launch {
//            _uiState.value = UiState.Loading
//            delay(1000) // Simulate loading delay
//            val filteredByName = filterByName(query, _allData.value)
//            if (filteredByName.isEmpty()) {
//                _emptySearchMessage.value =
//                    "No Name or division found for search query \"$query\"."
//            } else {
//                _emptySearchMessage.value = ""
//            }
//            _filteredTimeOffList.value = filteredByName.map { dataLeave ->
//                convertToTimeOffModel(dataLeave)
//            }
//            _uiState.value = UiState.Success(_filteredTimeOffList.value)
//        }
//    }
//
//    private fun searchByDateRange(range: Pair<Date?, Date?>) {
//        viewModelScope.launch {
//            _uiState.value = UiState.Loading
//            delay(500)
//            val filteredByDateRange = filterByDateRange(range, _allData.value)
//            if (filteredByDateRange.isEmpty()) {
//                val startDate = range.first?.toFormattedDate() ?: "N/A"
//                val endDate = range.second?.toFormattedDate() ?: "N/A"
//                _emptyDateRangeMessage.value = "No time off found from $startDate to $endDate."
//            } else {
//                _emptyDateRangeMessage.value = ""
//            }
//            _filteredTimeOffList.value = filteredByDateRange.map { dataLeave ->
//                convertToTimeOffModel(dataLeave)
//            }
//            _uiState.value = UiState.Success(_filteredTimeOffList.value)
//        }
//    }
//
//    private fun convertToTimeOffModel(dataLeave: DataLeave): TimeOffModel {
//        val baseUrl = "http://10.0.2.2:3000/"
//
//        val fullImageUrl = if (dataLeave.profilePhoto?.isNotEmpty() == true) {
//            baseUrl + dataLeave.profilePhoto
//        } else {
//            "https://w7.pngwing.com/pngs/177/551/png-transparent-user-interface-design-computer-icons-default-stephen-salazar-graphy-user-interface-design-computer-wallpaper-sphere-thumbnail.png"
//        }
//
//        return TimeOffModel(
//            id = dataLeave.leaveId ?: 0,
//            name = dataLeave.userName ?: "",
//            division = dataLeave.division ?: "",
//            leaveStartDate = dataLeave.startDate ?: "-",
//            leaveEndDate = dataLeave.endDate ?: "-",
//            submittedAt = dataLeave.submittedAt ?: "-",
//            photoProfile = fullImageUrl
//        )
//    }
//
//    private fun filterByName(query: String, dataList: List<DataLeave>): List<DataLeave> {
//        return dataList.filter {
//            it.userName?.contains(query, ignoreCase = true) == true ||
//                    it.division?.contains(query, ignoreCase = true) == true
//        }
//    }
//
//    private fun filterByDateRange(range: Pair<Date?, Date?>, dataList: List<DataLeave>): List<DataLeave> {
//        val (startDate, endDate) = range
//        return dataList.filter {
//            val leaveStart = dateFormat.parse(it.startDate ?: "")
//            val leaveEnd = dateFormat.parse(it.endDate ?: "")
//            (startDate == null || leaveStart >= startDate) && (endDate == null || leaveEnd <= endDate)
//        }
//    }
//
//    private fun applyFilters() {
//        val filteredByName = filterByName(_searchQuery.value, _allData.value)
//        val filteredByDateRange = filterByDateRange(_selectedDateRange.value, filteredByName)
//
//        val timeOffModels = filteredByDateRange.sortedByDescending {
//            dateFormat.parse(it.startDate ?: "").time
//        }.map { dataLeave ->
//            convertToTimeOffModel(dataLeave)
//        }
//
//        _filteredTimeOffList.value = timeOffModels
//    }
//}
//
//
//
