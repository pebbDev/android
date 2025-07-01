# Product Context: Infinite Track

Dokumen ini menjelaskan masalah yang ingin diselesaikan oleh proyek "Infinite Track", siapa saja yang terpengaruh, dan pengalaman ideal yang ingin diciptakan.

## 1. Latar Belakang Masalah (Why This Project Exists)

Sistem presensi yang ada sebelumnya di PT Kinema Systrans Multimedia (Infinite Learning) mengandalkan satu unit mesin *fingerprint* statis. Meskipun fungsional, sistem ini menciptakan beberapa masalah operasional yang signifikan seiring dengan berkembangnya perusahaan dan adopsi model kerja yang lebih modern.

Tujuan utama dari proyek "Infinite Track" adalah untuk **meningkatkan dan memodernisasi sistem lama** tersebut, dengan fokus utama untuk menciptakan **lingkungan kerja yang lebih fleksibel, efisien, dan terpercaya.**

## 2. Masalah yang Diselesaikan (User Pain Points)

Sistem yang lama menimbulkan kesulitan bagi dua kelompok pengguna utama:

### Untuk Karyawan & Mahasiswa Magang:
* **Waktu Tidak Efisien:** Terjadi antrean panjang pada jam-jam sibuk (masuk dan pulang kerja) karena semua orang harus menggunakan satu perangkat yang sama.
* **Kurangnya Fleksibilitas Lokasi:** Sistem tidak dapat mengakomodasi karyawan yang bekerja di gedung berbeda, dari rumah (WFH), atau dari lokasi lain (WFA), memaksa mereka untuk tetap datang ke titik absensi utama.
* **Proses yang Kaku:** Tidak ada mekanisme untuk menangani skenario kerja modern yang memerlukan mobilitas tinggi.

### Untuk Admin & Manajemen:
* **Proses Rekapitulasi Manual:** Data dari mesin *fingerprint* tidak terintegrasi dengan sistem pusat, sehingga admin harus melakukan rekapitulasi data secara manual ke dalam spreadsheet. Proses ini sangat memakan waktu dan rawan kesalahan (*human error*).
* **Kesulitan Verifikasi:** Tidak ada cara yang efisien untuk memverifikasi kehadiran atau keberadaan karyawan yang bekerja di luar kantor utama.
* **Kurangnya Wawasan Data:** Data yang ada sulit diolah menjadi laporan atau wawasan kinerja yang bermanfaat bagi manajemen.

## 3. Solusi & Pengalaman Ideal (The Solution & UX Goals)

"Infinite Track" dirancang sebagai platform terintegrasi (Aplikasi Android & Web Admin Panel) untuk mengatasi semua masalah di atas. Pengalaman pengguna (UX) yang ingin dicapai berpusat pada tiga pilar:

1.  **Cepat & Mudah (*Seamless*):**
    * Pengguna dapat melakukan absensi dalam hitungan detik melalui ponsel mereka masing-masing, menghilangkan antrean sepenuhnya.
    * Antarmuka dirancang agar intuitif dan mudah digunakan.

2.  **Fleksibel (*Flexible*):**
    * Sistem secara penuh mendukung tiga mode kerja: **WFO, WFH, dan WFA**.
    * Pengguna memiliki kebebasan untuk bekerja dari lokasi yang paling produktif bagi mereka, dengan alur kerja yang jelas (seperti sistem booking WFA) untuk menjaga akuntabilitas.

3.  **Terpercaya (*Trustworthy*):**
    * Manajemen dapat mempercayai data kehadiran berkat validasi berlapis.
    * **Geofencing** memastikan absensi dilakukan dari lokasi yang benar.
    * **Face Recognition** dengan **Liveness Detection** memastikan identitas pengguna dan mencegah praktik "titip absen".
    * **Algoritma cerdas** di backend menganalisis anomali dan memberikan skor, menambah lapisan kepercayaan pada data.

## 4. Metrik Keberhasilan Produk

Proyek ini dianggap berhasil jika mampu mencapai tujuan-tujuan berikut:
-   Mengurangi waktu antre absensi di kantor hingga **nol**.
-   Mengurangi waktu yang dihabiskan admin untuk rekapitulasi data kehadiran secara signifikan **(target > 90%)**.
-   Meningkatkan **fleksibilitas dan kepuasan** karyawan dalam bekerja.
-   Meningkatkan **akurasi dan integritas** data kehadiran, yang dapat diandalkan untuk analisis kinerja dan kebutuhan administratif lainnya.
