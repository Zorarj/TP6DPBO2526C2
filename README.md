# Flappy Bird - Java Swing

Proyek ini adalah implementasi dari game klasik Flappy Bird yang dibangun menggunakan bahasa pemrograman Java dan pustaka grafis Java Swing. Game ini mencakup mekanik fisika sederhana, deteksi tabrakan, antarmuka pengguna interaktif, dan penambahan audio.

## Fitur (Spesifikasi)
* **Main Menu:** Tampilan awal dengan tombol "Start Game" untuk memulai dan "Exit" untuk keluar dari aplikasi.
* **Sistem Skor:** Skor (menggunakan `JLabel`) ditampilkan pada layar secara real-time dan akan bertambah setiap kali burung berhasil melewati sepasang pipa.
* **Deteksi Tabrakan (Game Over):** Permainan akan otomatis berhenti jika karakter menabrak/menyentuh pipa atau jatuh menyentuh batas bawah layar (JFrame).
* **Restart Permainan:** Saat berada di layar Game Over, pemain dapat memulai ulang permainan dari awal dengan menekan tombol **R** pada keyboard.
* **Audio BGM (Bonus):** Dilengkapi dengan musik latar (BGM) yang berputar terus-menerus (looping) selama aplikasi berjalan.

## Prasyarat
* Java Development Kit (JDK) versi 8 atau yang lebih baru.
* Asset gambar (`bird.png`, `lowerPipe.png`, `upperPipe.png`) dan audio (`bgm.wav`).

## Struktur File
Pastikan direktori proyek memiliki penataan struktur sebagai berikut agar aset dapat terbaca dengan benar:
```text
├── App.java        (Kelas utama, entry point, dan UI Main Menu)
├── Logic.java      (Logika pergerakan, gravitasi, tabrakan, skor, dan pemutar audio)
├── View.java       (Panel rendering grafis/UI arena permainan)
├── Player.java     (Objek entitas karakter burung)
├── Pipe.java       (Objek rintangan pipa atas dan bawah)
└── assets/         (Direktori aset wajib)
    ├── bird.png
    ├── lowerPipe.png
    ├── upperPipe.png
    └── bgm.wav
```

## Cara Menjalankan
1. Pastikan seluruh file source code `.java` dan folder `assets/` berada pada struktur direktori yang tepat.
2. Buka terminal atau *command prompt*, lalu arahkan ke direktori proyek.
3. Kompilasi seluruh kelas Java dengan perintah:
   ```bash
   javac *.java
   ```
4. Jalankan aplikasi melalui kelas utama dengan perintah:
   ```bash
   java App
   ```

## Kontrol Permainan
* **Tombol Antarmuka (Klik Mouse)**
  * **Start Game:** Ditekan menggunakan *mouse* pada layar awal untuk menghancurkan menu dan memuat panel permainan.
  * **Exit:** Ditekan menggunakan *mouse* untuk mematikan dan keluar dari program secara keseluruhan.
* **Tombol Keyboard**
  * **Spasi (Spacebar):** Memberikan dorongan kecepatan ke atas (melompat/terbang) pada burung. Tombol ini hanya berfungsi jika permainan sedang aktif.
  * **R (Keyboard):** Memulai ulang (restart) permainan. Tombol ini hanya berfungsi ketika pemain sudah mendapatkan status **Game Over**.

## Alur Program (Flow Aplikasi)
1. **Inisialisasi & Main Menu (`App.java`):** Saat program pertama kali dijalankan, aplikasi membuka jendela (JFrame) yang menampilkan tombol **Start Game** dan **Exit**. Objek permainan belum dimuat untuk menghemat memori.
2. **Memulai Permainan:** Ketika tombol **Start Game** diklik, panel menu dihapus. Program menginisialisasi kelas `Logic` (aturan fisika & skor) dan `View` (rendering visual). Audio BGM mulai diputar *looping*, dan *Timer* permainan berjalan (60 FPS).
3. **Looping Permainan & Gravitasi (`Logic.java`):** Selama bermain, fungsi `move()` dipanggil terus-menerus. Burung selalu jatuh ke bawah akibat `gravity` pada Velocity Y. Pipa dihasilkan setiap 1,5 detik (melalui *Timer*) dan bergerak ke kiri (Velocity X).
4. **Penghitungan Skor:** Program mengecek posisi burung. Jika berhasil melewati batas kanan pipa yang belum dilewati, skor bertambah (+1 untuk satu pasang pipa) dan `JLabel` skor diperbarui.
5. **Kondisi Game Over:** Setiap frame, program mengecek fungsi `collision()`. Jika burung bersinggungan dengan pipa atau menyentuh batas bawah layar, `gameOver` menjadi `true`. *Timer* berhenti sementara, dan teks "GAME OVER" muncul di layar.
6. **Restart Permainan:** Saat status `gameOver`, menekan tombol **R** akan mengatur ulang posisi burung, skor (kembali 0), dan membersihkan pipa di layar. *Timer* berjalan kembali tanpa menutup aplikasi.