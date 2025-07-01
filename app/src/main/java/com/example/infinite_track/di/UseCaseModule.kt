package com.example.infinite_track.di

import com.example.infinite_track.data.face.FaceProcessor
import com.example.infinite_track.domain.repository.AttendanceHistoryRepository
import com.example.infinite_track.domain.repository.AuthRepository
import com.example.infinite_track.domain.repository.ContactRepository
import com.example.infinite_track.domain.repository.LocalizationRepository
import com.example.infinite_track.domain.repository.LocationRepository
import com.example.infinite_track.domain.repository.ProfileRepository
import com.example.infinite_track.domain.use_case.auth.CheckSessionUseCase
import com.example.infinite_track.domain.use_case.auth.GetLoggedInUserUseCase
import com.example.infinite_track.domain.use_case.auth.LoginUseCase
import com.example.infinite_track.domain.use_case.auth.LogoutUseCase
import com.example.infinite_track.domain.use_case.contact.GetContactsUseCase
import com.example.infinite_track.domain.use_case.history.GetAttendanceHistoryUseCase
import com.example.infinite_track.domain.use_case.language.GetSelectedLanguageUseCase
import com.example.infinite_track.domain.use_case.language.SetSelectedLanguageUseCase
import com.example.infinite_track.domain.use_case.location.GetCurrentAddressUseCase
import com.example.infinite_track.domain.use_case.profile.UpdateProfileUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    // Provide the Login Use Case
    @Provides
    fun provideLoginUseCase(
        authRepository: AuthRepository,
        faceProcessor: FaceProcessor
    ): LoginUseCase {
        return LoginUseCase(authRepository, faceProcessor)
    }

    // Provide the Check Session Use Case
    @Provides
    fun provideCheckSessionUseCase(
        authRepository: AuthRepository,
        faceProcessor: FaceProcessor
    ): CheckSessionUseCase {
        return CheckSessionUseCase(authRepository, faceProcessor)
    }

    // Provide the Logout Use Case
    @Provides
    fun provideLogoutUseCase(authRepository: AuthRepository): LogoutUseCase {
        return LogoutUseCase(authRepository)
    }

    // Provide the Contacts Use Case
    @Provides
    fun provideGetContactsUseCase(contactRepository: ContactRepository): GetContactsUseCase {
        return GetContactsUseCase(contactRepository)
    }

    // Provide the GetLoggedInUser Use Case
    @Provides
    fun provideGetLoggedInUserUseCase(authRepository: AuthRepository): GetLoggedInUserUseCase {
        return GetLoggedInUserUseCase(authRepository)
    }

    // Provide the Language Use Cases
    @Provides
    fun provideGetSelectedLanguageUseCase(localizationRepository: LocalizationRepository): GetSelectedLanguageUseCase {
        return GetSelectedLanguageUseCase(localizationRepository)
    }

    @Provides
    fun provideSetSelectedLanguageUseCase(localizationRepository: LocalizationRepository): SetSelectedLanguageUseCase {
        return SetSelectedLanguageUseCase(localizationRepository)
    }

    // Provide the Update Profile Use Case
    @Provides
    fun provideUpdateProfileUseCase(profileRepository: ProfileRepository): UpdateProfileUseCase {
        return UpdateProfileUseCase(profileRepository)
    }

    // Provide the Attendance History Use Case
    @Provides
    fun provideGetAttendanceHistoryUseCase(
        attendanceHistoryRepository: AttendanceHistoryRepository
    ): GetAttendanceHistoryUseCase {
        return GetAttendanceHistoryUseCase(attendanceHistoryRepository)
    }

    // Provide the Get Current Address Use Case
    @Provides
    fun provideGetCurrentAddressUseCase(locationRepository: LocationRepository): GetCurrentAddressUseCase {
        return GetCurrentAddressUseCase(locationRepository)
    }
}
