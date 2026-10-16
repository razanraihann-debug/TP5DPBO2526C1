# Janji
Saya Razan Raihan Malik dengan NIM 2508838 mengerjakan Tugas Praktikum 5 pada Mata Kuliah Desain dan Pemrograman Berorientasi Objek (DPBO) untuk keberkahan-Nya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin
# Struktur File
<img width="399" height="253" alt="Screenshot 2026-10-16 at 09 08 01" src="https://github.com/user-attachments/assets/d1679906-a09d-4aee-8b6e-9ebd0d136cf5" />

# Desain / Fungsi Program
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

# Alur Program
1. Program dijalankan → `refreshTable()` mengambil semua data dari tabel `tb_kru` → ditampilkan di `JTable`.
2. **Create** – isi form, klik **Add** → `INSERT`. Dialog error muncul jika **ada kolom kosong** atau **ID sudah ada** di database.
3. **Read** – seluruh data tampil di tabel. Klik satu baris untuk memuat nilainya ke form lewat model tabel (mode edit: Add nonaktif, Update & Delete aktif, ID dikunci).
4. **Update** – ubah isi form, klik **Update** → `UPDATE` (dialog error jika ada kolom kosong).
5. **Delete** – klik **Delete** → dialog konfirmasi (Yes/No) → `DELETE` jika memilih *Yes*.
6. **Cancel** – mengosongkan form & membatalkan mode edit.

# Dokumentasi
[![YouTube](https://img.shields.io/badge/YouTube-FF0000?style=for-the-badge&logo=youtube&logoColor=white)](https://www.youtube.com/watch?v=4gRAuA6A8bM)

klik untuk melihat video
