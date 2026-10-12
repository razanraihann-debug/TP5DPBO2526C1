-- Jalankan file ini di MySQL (phpMyAdmin / MySQL Workbench / terminal)
CREATE DATABASE IF NOT EXISTS db_antariksa;
USE db_antariksa;

DROP TABLE IF EXISTS tb_kru;
CREATE TABLE tb_kru (
    id           VARCHAR(20)  NOT NULL PRIMARY KEY,
    nama         VARCHAR(100) NOT NULL,
    tahun_lahir  INT          NOT NULL,
    kategori     VARCHAR(50)  NOT NULL,
    level_energi INT          NOT NULL DEFAULT 50
);

INSERT INTO tb_kru (id, nama, tahun_lahir, kategori, level_energi) VALUES
('KRU001', 'Laksmana Aditya',     1985, 'Komandan Misi',      92),
('KRU002', 'Nayla Prameswari',    1991, 'Pilot Roket',        85),
('KRU003', 'Bima Saputra',        1988, 'Insinyur Reaktor',   70),
('KRU004', 'Dr. Kirana Larasati', 1990, 'Astrobiolog',        64),
('KRU005', 'Rangga Wiratama',     1994, 'Dokter Antariksa',   78),
('KRU006', 'Sekar Ayuningtyas',   1996, 'Teknisi Hidroponik', 55),
('KRU007', 'Galih Nusantara',     1993, 'Pilot Roket',        88),
('KRU008', 'Tara Wulandari',      1998, 'Teknisi Hidroponik', 40);
