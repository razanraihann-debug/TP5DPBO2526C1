# Data Kru Stasiun Antariksa (PeopleData)

Tugas Praktikum 5 – DPBO. Aplikasi desktop Java Swing (CRUD) + **database MySQL** untuk mengelola data kru **Stasiun Antariksa Nusantara-1**.

## Desain / Fungsi Program

Tema **People** divisualisasikan sebagai kru stasiun luar angkasa. Setiap `Person` memiliki atribut:

| Atribut | Komponen Form | Keterangan |
|---|---|---|
| ID | `JTextField` | Kode unik kru (mis. `KRU001`), tidak bisa diubah saat edit |
| Nama | `JTextField` | Nama lengkap kru |
| Tahun Lahir | `JTextField` | Angka 1900 – tahun sekarang |
| Kategori | `JComboBox` | Jabatan: Komandan Misi, Pilot Roket, Insinyur Reaktor, Astrobiolog, Dokter Antariksa, Teknisi Hidroponik |
| **Level Energi** *(atribut baru)* | **`JSlider`** | Nilai 0–100%, nilainya tampil real-time di samping slider (**bonus +20**, bukan `JTextField`) |

Struktur kode:

- `Person.java` – class model (atribut, constructor, getter/setter).
- `Database.java` – koneksi JDBC ke MySQL + query CRUD (`PreparedStatement`).
- `PeopleMenu.java` – form utama (GUI + logika CRUD). Data dibaca langsung dari database; tidak ada lagi `ArrayList`.
- `Database/db_antariksa.sql` – skrip pembuat database, tabel `tb_kru`, dan 8 data awal.
- `PeopleMenu.form` – desain form (IntelliJ IDEA GUI Designer), ter-bind ke class `PeopleMenu`.

## Alur Program

1. Program dijalankan → `refreshTable()` mengambil semua data dari tabel `tb_kru` → ditampilkan di `JTable`.
2. **Create** – isi form, klik **Add** → `INSERT`. Dialog error muncul jika **ada kolom kosong** atau **ID sudah ada** di database.
3. **Read** – seluruh data tampil di tabel. Klik satu baris untuk memuat nilainya ke form lewat model tabel (mode edit: Add nonaktif, Update & Delete aktif, ID dikunci).
4. **Update** – ubah isi form, klik **Update** → `UPDATE` (dialog error jika ada kolom kosong).
5. **Delete** – klik **Delete** → dialog konfirmasi (Yes/No) → `DELETE` jika memilih *Yes*.
6. **Cancel** – mengosongkan form & membatalkan mode edit.

## Cara Menjalankan

1. Nyalakan MySQL (XAMPP/Laragon/MySQL Server), lalu import `Database/db_antariksa.sql`.
2. Sesuaikan `URL`, `USER`, `PASS` di `PeopleData/Database.java` jika perlu (default: `root`, tanpa password, port 3306).
3. **IntelliJ IDEA:** tambahkan driver lewat *File → Project Structure → Libraries → + → From Maven* → `com.mysql:mysql-connector-j:8.4.0`.
4. Jalankan `main()` pada `PeopleMenu`.

## Dokumentasi

> Ganti gambar di bawah dengan screenshot/GIF hasil run programmu (simpan di folder `Dokumentasi/`).

| Operasi | Tampilan |
|---|---|
| Tampilan awal (Read) | ![Awal](Dokumentasi/01-awal.png) |
| Create (Add) | ![Create](Dokumentasi/02-create.png) |
| Update | ![Update](Dokumentasi/03-update.png) |
| Konfirmasi Delete | ![Konfirmasi](Dokumentasi/04-konfirmasi-delete.png) |
| Setelah Delete | ![Delete](Dokumentasi/05-delete.png) |
