package com.example.infinite_track.data.soucre.repository.contact

//import android.util.Log
//import com.example.infinite_track.data.soucre.dummy.ContactItem
//import com.example.infinite_track.data.soucre.dummy.DummyData
//import com.example.infinite_track.data.soucre.network.response.ContactsResponse
//import com.example.infinite_track.data.soucre.network.response.ErrorResponse
//import com.example.infinite_track.data.soucre.network.retrofit.ApiService
//import com.example.infinite_track.utils.UiState
//import com.google.gson.Gson
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.flow
//import javax.inject.Inject
//import javax.inject.Singleton

//@Singleton
//class ContactsRepository @Inject constructor(
//    private val apiService: ApiService,
//) {
//    // Function that returns dummy contacts data from our new DummyData class
//    fun getContacts(): Flow<UiState<List<ContactItem>>> = flow {
//        emit(UiState.Loading)
//        try {
//            // Use dummy data directly from DummyData.kt
//            emit(UiState.Success(DummyData.contacts))
//        } catch (e: Exception) {
//            Log.e("ContactsRepository", "Error getting contacts: ${e.message}")
//            emit(UiState.Error(e.message ?: "Unknown error occurred"))
//        }
//    }
//
//    // Keep the original method for API calls (renamed to avoid conflict)
//    suspend fun getContactsFromApi(): Flow<ContactsResponse> {
//        try {
//            val response = apiService.getContacts()
//            return flow { emit(response) }
//        } catch (e: retrofit2.HttpException) {
//            e.printStackTrace()
//            val jsonInString = e.response()?.errorBody()?.string()
//            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
//            val errorMessage = errorBody?.message ?: "Unknown Error"
//            Log.d("Contact Repository", "Get All Contacts: $errorMessage")
//            throw e
//        }
//    }
//}