# System Patterns: Infinite Track

Dokumen ini menjelaskan pola arsitektur, alur data, dan keputusan desain kunci yang menjadi fondasi dari aplikasi Android Infinite Track.

## 1. Arsitektur Umum: Clean Architecture

Aplikasi ini secara ketat mengadopsi prinsip-prinsip **Clean Architecture** untuk memastikan pemisahan tanggung jawab (separation of concerns), skalabilitas, dan kemudahan pengujian. Arsitektur ini dibagi menjadi tiga lapisan utama:

-   **`presentation`**: Bertanggung jawab atas semua yang terkait dengan UI.
-   **`domain`**: Berisi logika bisnis inti aplikasi.
-   **`data`**: Bertanggung jawab atas sumber dan penyimpanan data.

Dependensi antar lapisan mengikuti satu arah yang ketat: `presentation` -> `domain` -> `data`.

### Alur Data Standar
Aliran data untuk setiap fitur mengikuti pola berikut:
**`UI (Screen)` -> `ViewModel` -> `UseCase` -> `Repository (Interface)` -> `RepositoryImpl` -> `Sumber Data (API/DB)`**

## 2. Pola State Management: `UiState`

Untuk menangani operasi asinkron (seperti panggilan API) dan mengomunikasikan hasilnya ke UI, kami secara konsisten menggunakan pola **`UiState`**.

-   Sebuah `sealed class UiState<T>` digunakan untuk merepresentasikan semua kemungkinan kondisi:
    -   `Loading`: Saat data sedang diambil.
    -   `Success(data: T)`: Saat data berhasil didapat.
    -   `Error(message: String)`: Saat terjadi kesalahan.
    -   `Idle` (opsional): State awal sebelum ada aksi.
-   `ViewModel` akan mengekspos `StateFlow<UiState<T>>` yang akan diobservasi oleh UI untuk menampilkan tampilan yang sesuai secara reaktif.

## 3. Pola Navigasi: `Single-Activity` dengan `Nested Graph`

Sistem navigasi dibangun di atas **Navigation for Compose** dengan pola sebagai berikut:

1.  **Single-Activity Architecture**: Aplikasi hanya memiliki satu `MainActivity` yang bertindak sebagai container utama.
2.  **`NavHost` Bertingkat**:
    * `NavHost` di level tertinggi (`InfiniteTrackApp`) hanya memiliki dua tujuan utama: grafik otentikasi (`auth_graph`) dan grafik utama (`main_graph`).
    * `SplashScreen` bertindak sebagai gerbang yang mengarahkan pengguna ke salah satu dari dua grafik tersebut.
    * Grafik utama (`main_graph`) berisi `NavHost`-nya sendiri untuk menangani navigasi antar layar setelah login (`Home`, `Profile`, `History`, dll.).
3.  **Shared ViewModel**: Untuk fitur yang mencakup beberapa layar (seperti `History` dan `Profile`), kami menggunakan *scoped ViewModel* yang terikat pada *lifecycle* grafik navigasi induknya (`history_flow`, `profile_flow`). Ini memastikan state tetap konsisten saat bernavigasi antar layar terkait.

## 4. Pola Sumber Data: SSoT dengan Room

Strategi pengelolaan data kami berpusat pada prinsip **Single Source of Truth (SSoT)**.

-   **Room Database**: Bertindak sebagai **Single Source of Truth** untuk semua data yang bersifat persisten (seperti profil pengguna dan cache data lainnya). Lapisan UI (`ViewModel` dan `Screen`) akan **selalu** membaca data dari Room untuk memastikan UI yang cepat, responsif, dan berfungsi secara offline.
-   **Jetpack DataStore**: Digunakan secara khusus hanya untuk menyimpan data sesi yang sederhana dan primitif, yaitu **`token`** dan **`userId`**.
-   **Repository**: Bertanggung jawab sebagai satu-satunya perantara ke sumber data. Tugasnya adalah menyinkronkan data dari `ApiService` (jaringan) ke dalam `Room` (lokal).
-   **Mapper**: Fungsi pemetaan digunakan untuk mengonversi antara DTO (dari API), `Domain Model` (untuk `UseCase`), dan `Entity` (untuk Room).
