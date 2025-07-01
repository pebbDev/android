//package com.example.infinite_track.data.soucre.repository.profile
//
//import com.example.infinite_track.data.soucre.local.preferences.UserPreference
//import com.example.infinite_track.data.soucre.network.request.ProfileRequest
//import com.example.infinite_track.data.soucre.network.response.ProfileResponse
//import com.example.infinite_track.data.soucre.network.retrofit.ApiService
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.first
//import javax.inject.Inject
//
//class ProfileRepository @Inject constructor(
//    private val pref: UserPreference,
//    private val apiService: ApiService,
//) {
//
//    fun getUser(): Flow<UserModel> {
//        return pref.getUser()
//    }
//
//    suspend fun updateUserProfile(userId: Int, profileRequest: ProfileRequest): ProfileResponse {
//        val response = apiService.updateUser(userId, profileRequest)
//        if (response.isSuccessful) {
//            val updatedProfile = response.body()!!
//            val currentUser = getUser().first()
//            val updatedUser = currentUser.copy(
//                address = profileRequest.address ?: currentUser.address,
//                phone_number = profileRequest.phone_number ?: currentUser.phone_number,
//                nip_nim = profileRequest.nip_nim ?: currentUser.nip_nim,
//                start_contract = profileRequest.start_contract ?: currentUser.start_contract,
//                end_contract = profileRequest.end_contract ?: currentUser.end_contract
//            )
//            pref.updatePartialUser(
//                address = updatedUser.address,
//                phone_number = updatedUser.phone_number,
//                nip_nim = updatedUser.nip_nim,
//                start_contract = updatedUser.start_contract,
//                end_contract = updatedUser.end_contract
//            )
//            return updatedProfile
//        } else {
//            throw Exception("Failed to update profile: ${response.message()}")
//        }
//    }
//}