# Tech Context: Infinite Track

Dokumen ini mencatat semua teknologi, library, dan alat bantu yang digunakan dalam pengembangan proyek Infinite Track.

## 1. Tumpukan Teknologi Backend

-   **Runtime**: Node.js `v20.18.0`
-   **Framework**: Express.js
-   **Database**: MySQL
-   **Otentikasi**: JWT (JSON Web Tokens)
-   **Penyimpanan File**: Cloudinary
-   **API Geospasial**: Geoapify
-   **Logika Cerdas**: Fuzzy Logic & AHP (diimplementasikan di backend)

## 2. Tumpukan Teknologi Android

### Arsitektur & Pola
-   **Arsitektur Utama**: Clean Architecture (`data` -> `domain` -> `presentation`)
-   **Pola Desain**: MVVM (Model-View-ViewModel), Repository Pattern, UseCase
-   **Dependency Injection**: Hilt

### Versi & Konfigurasi Build
-   **Kotlin Plugin Version**: `1.9.0`
-   **Android Gradle Plugin (AGP) Version**: `8.5.2`

### Library Kunci
-   **UI**: Jetpack Compose
-   **Navigasi**: Navigation for Compose
-   **Networking**: Retrofit & OkHttp
-   **Asynchronous**: Kotlin Coroutines & Flow (`StateFlow`)
-   **Database Lokal**: Room Persistence Library
-   **Penyimpanan Sesi**: Jetpack DataStore (Preferences)
-   **Peta**: Mapbox Maps SDK & Google Maps Compose
-   **Pemuatan Gambar**: Coil (Coil Compose)
-   **Machine Learning**: TensorFlow Lite (dengan GPU acceleration dan Task Vision)
-   **Notifikasi**: Firebase Cloud Messaging (FCM) & `core-splashscreen` API
-   **Geofencing**: Google Play Services Location
-   **Animasi**: Lottie for Compose
-   **Kamera**: CameraX
-   **Permissions**: Accompanist Permissions

## 3. Alat Bantu Pengembangan & Desain

-   **Version Control**: Git & GitHub
-   **Desain UI/UX**: Figma
-   **Manajemen Proyek**: Trello
