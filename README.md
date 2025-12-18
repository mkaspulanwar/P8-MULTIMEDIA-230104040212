# PRAKTIKUM PEMPROGRAMAN MOBILE #8: ANDROID MULTIMEDIA APP

<div align="center">
  <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" />
  <img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" />
  <img src="https://img.shields.io/badge/Jetpack_Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" />
  <img src="https://img.shields.io/badge/Material_3-757575?style=for-the-badge&logo=materialdesign&logoColor=white" />  
  <img src="https://img.shields.io/badge/ExoPlayer-Media3-FF6F00?style=for-the-badge&logo=googleplay&logoColor=white" />
  <img src="https://img.shields.io/badge/MediaStore-Scoped_Storage-4CAF50?style=for-the-badge" />
</div>

---

Aplikasi Android modern yang dibangun menggunakan **Jetpack Compose** untuk mengelola multimedia secara komprehensif. Proyek ini merupakan hasil dari **Praktikum #8 Mobile Programming 20251** dengan fokus pada integrasi Kamera, Galeri, Audio Recorder, dan Video Player.

## Tujuan Praktikum
Proyek ini bertujuan untuk mendemonstrasikan kemampuan dalam:
* **Pengelolaan Multimedia:** Memahami konsep audio, video, dan gambar pada ekosistem Android.
* **Interactive UI:** Mengimplementasikan gesture handling (pinch zoom & pan) pada media.
* **State Management:** Menangani perubahan orientasi layar tanpa kehilangan data media.
* **Modern API Integration:** Menggunakan MediaStore (Scoped Storage) dan ExoPlayer (Media3).
* **Clean Architecture:** Menerapkan struktur folder yang terpisah antara UI, Navigation, dan Utility.

---

## Tim Developer

| Peran | Nama | NIM | Profil GitHub |
| :--- | :--- | :--- | :--- |
| **Pengembang Proyek** | M. Kaspul Anwar | 230104040212 | [![](https://img.shields.io/badge/GitHub-M.KaspulAnwar-181717?style=flat&logo=github)](https://github.com/mkaspulanwar) |
| **Dosen Pengampu** | Muhayat, M. IT | - | [![](https://img.shields.io/badge/GitHub-Muhayat,M.IT-181717?style=flat&logo=github)](https://github.com/muhayat-lab) |

---

## Fitur Utama

| Modul | Fitur Unggulan | Deskripsi |
| :--- | :--- | :--- |
| **Camera & Gallery** | Direct Preview | Mengambil foto/video tanpa berpindah screen. |
| **Interactive Media** | Gesture Support | Fitur *Pinch-to-Zoom* dan *Pan/Geser* pada gambar dan video. |
| **Audio System** | Recorder & Player | Perekaman suara real-time dan pemutaran list audio hasil rekaman. |
| **Video Player** | ExoPlayer Media3 | Pemutaran video dengan dukungan Fullscreen mode dan gesture. |
| **Storage** | Scoped Storage | Simpan media secara permanen ke Galeri menggunakan MediaStore. |
| **Responsiveness** | Orientation Aware | Layout adaptif untuk mode Portrait maupun Landscape. |

---

## Stack Teknologi & Tools

### Development Tools
* **Android Studio:** IDE Utama untuk pengembangan.
* **Android SDK:** API Level 24 (Min) hingga Level 34 (Target).
* **Gradle:** Build system & dependency management.

### Libraries
* **Jetpack Compose & Navigation:** Framework UI deklaratif dan navigasi antar layar.
* **Material 3:** Komponen desain modern (Cards, Buttons, TopAppBar).
* **Media3 ExoPlayer:** Library engine untuk pemutaran video berkualitas tinggi.
* **Pointer Input API:** Menangani interaksi sentuh tingkat lanjut.

---

## Struktur Proyek
Aplikasi ini mengikuti standar arsitektur UI yang terorganisir:
* `ui/` : Berisi komponen Composable untuk setiap layar (Home, Gallery, Audio, Video).
* `navigation/` : Pengaturan rute dan perpindahan antar screen.
* `util/` : Logic helper untuk pengelolaan file dan MediaStore.
* `theme/` : Definisi warna, tipografi, dan tema Material 3.

---

##  Cara Menjalankan
1.  Clone repository ini:
    ```bash
    git clone [https://github.com/username/praktikum8-multimedia.git](https://github.com/username/praktikum8-multimedia.git)
    ```
2.  Buka project di **Android Studio**.
3.  Pastikan menggunakan **JDK 17**.
4.  Hubungkan Real Device (disarankan untuk fitur Kamera/Audio) atau jalankan Emulator.
5.  Klik **Run 'app'**.

---

## 📝 Pengelolaan File
Aplikasi ini mendukung manajemen file multimedia yang aman meliputi:
1.  **Pengambilan Daftar File:** Membaca metadata file multimedia.
2.  **Pemutaran Media:** Integrasi sistem playback yang responsif.
3.  **File Operations:** Fitur untuk menghapus dan mengubah nama (*rename*) file secara langsung.

---
**Dosen Pengampu:** Muhayat, M.IT  
**Status:** Praktikum Selesai ✅
