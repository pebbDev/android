package com.example.infinite_track.di

import android.os.Build
import android.util.Log
import com.example.infinite_track.data.soucre.local.preferences.UserPreference
import com.example.infinite_track.data.soucre.network.retrofit.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // 1. Menyediakan Interceptor untuk Logging
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    // 2. Menyediakan Interceptor untuk Otentikasi
    @Provides
    @Singleton
    fun provideAuthInterceptor(userPreference: UserPreference): Interceptor {
        return Interceptor { chain ->
            // Mengambil token secara sinkron dari DataStore
            val token = runBlocking { userPreference.getAuthToken().first() }
            val requestBuilder = chain.request().newBuilder()

            if (!token.isNullOrEmpty()) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }

            chain.proceed(requestBuilder.build())
        }
    }

    // 3. Menyediakan OkHttpClient dengan timeout yang lebih panjang
    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS) // Increased timeout
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    // 4. Menyediakan Retrofit dengan deteksi emulator secara runtime
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    // Helper method for reliable emulator detection that worked for you
    private fun isEmulator(): Boolean {
        return Build.FINGERPRINT.contains("generic") || Build.FINGERPRINT.contains("emulator")
    }

    // Property that returns the appropriate baseUrl based on device type
    private val baseUrl: String
        get() = if (isEmulator()) {
            "http://10.0.2.2:3005/"
        } else {
            "http://192.168.208.197:3005/"
        }
}