# Active Context: Infinite Track

Dokumen ini berisi fokus kerja, keputusan terbaru, dan langkah selanjutnya yang relevan untuk sesi pengembangan saat ini.

*Terakhir diperbarui: 30 Juni 2025*

---

## 1. Fokus Utama Saat Ini

Prioritas utama kita saat ini adalah **menyelesaikan konfigurasi dan integrasi untuk `HomeScreen`**. Tujuannya adalah membuat `HomeScreen` sepenuhnya fungsional dengan menampilkan data yang benar sesuai dengan peran pengguna, yang disediakan oleh `HomeViewModel` terpusat.

**Target Selesai untuk Sesi Ini:**
-   `HomeScreen` berhasil mengobservasi semua state dari `HomeViewModel`.
-   `EmployeeAndManagerContent` menampilkan data profil (dari Room), saldo cuti (dummy), dan 5 riwayat absensi teratas (dari API).
-   `InternshipContent` menampilkan data profil (dari Room) dan ringkasan absensi (menggunakan data statis atau dari API, tergantung kesiapan).

## 2. Pola & Preferensi Aktif

Berdasarkan diskusi terakhir, kita menyepakati beberapa pola kerja yang akan diterapkan secara konsisten:
-   **Arsitektur Tetap `Clean`**: Alur `UI -> ViewModel -> UseCase -> Repository` akan terus dipertahankan.
-   **State Management Fleksibel**:
    -   Untuk data yang diambil dari jaringan dan bisa gagal (misal: riwayat absensi), kita akan menggunakan `sealed class UiState<T>` yang berisi `Loading`, `Success`, dan `Error`.
    -   Untuk data yang sudah pasti ada atau berasal dari cache lokal (misal: profil pengguna dari Room atau data dummy), kita akan langsung menggunakan `StateFlow<T>` untuk menjaga `ViewModel` tetap sederhana.
-   **UI "Dumb"**: Komponen konten seperti `EmployeeAndManagerComponent` dan `InternshipContent` harus tetap "bodoh", artinya hanya menerima state dari atasannya (`HomeScreen`) dan tidak memiliki `ViewModel`-nya sendiri.

## 3. Masalah yang Diketahui & Sedang Dikerjakan

-   **Isu Utama**: Terdapat ketidakcocokan data dan beberapa error kompilasi saat mencoba menghubungkan `HomeViewModel` ke `EmployeeAndManagerComponent` dan `InternshipContent`.
-   **Solusi yang Dijalankan**: Kita akan merefactor `HomeViewModel` dan komponen-komponen konten tersebut agar `ViewModel` yang bertanggung jawab untuk memproses data (mengambil 5 item teratas, dll.) dan UI hanya menampilkannya.

## 4. Langkah Selanjutnya (Next Steps)

Setelah `HomeScreen` berhasil diimplementasikan, fokus kita selanjutnya adalah:
1.  **Menyelesaikan Alur Otentikasi**: Mengintegrasikan `LoginViewModel` dan alur logout di `ProfileViewModel` secara penuh.
2.  **Menyelesaikan Fitur Absensi**: Mengimplementasikan `AttendanceScreen` dengan fungsionalitas Geofencing dan `CameraX` untuk Face Recognition.
3.  **Menyelesaikan Fitur Lainnya**: `HistoryScreen` (dengan paginasi), `EditProfile`, dll.
