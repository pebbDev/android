# Active Context: Infinite Track

Dokumen ini berisi fokus kerja, keputusan terbaru, dan langkah selanjutnya yang relevan untuk sesi pengembangan saat ini.

*Terakhir diperbarui: 1 Juli 2025*

---

## 1. Fokus Utama Saat Ini

Prioritas utama kita adalah **membangun `AttendanceScreen`**. Fokusnya adalah pada penyiapan UI, menyatukan komponen, dan mengintegrasikannya ke dalam alur navigasi.

**Target Selesai untuk Sesi Ini:**
-   Komponen `AttendanceMap` telah direfaktor untuk menangani izin melalui `ViewModel`.
-   Komponen `TopAppBar` kustom telah dibuat.
-   `AttendanceScreen` telah dirakit menggunakan `Box` layout.
-   Navigasi dari `HomeScreen` ke `AttendanceScreen` telah dibuat.

## 2. Langkah Selanjutnya (Next Steps)

Setelah fondasi UI dan navigasi selesai, langkah selanjutnya adalah:
1.  Mengintegrasikan `AttendanceViewModel` untuk mengambil data status.
2.  Membangun `BottomSheet` yang dinamis.
3.  Mengimplementasikan logika *Face Recognition*.
