-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versi server:                 8.4.3 - MySQL Community Server - GPL
-- OS Server:                    Win64
-- HeidiSQL Versi:               12.8.0.6908
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Membuang struktur basisdata untuk spkhop
CREATE DATABASE IF NOT EXISTS `spkhop` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `spkhop`;

-- membuang struktur untuk table spkhop.alternatif
CREATE TABLE IF NOT EXISTS `alternatif` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nik` varchar(50) DEFAULT NULL,
  `nama` varchar(50) DEFAULT NULL,
  `jabatan` varchar(50) DEFAULT NULL,
  `alamat` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Membuang data untuk tabel spkhop.alternatif: ~6 rows (lebih kurang)
INSERT INTO `alternatif` (`id`, `nik`, `nama`, `jabatan`, `alamat`) VALUES
	(1, '1', 'GUNAWAN NASUTION', 'Manager Teknik', 'Jakarta Timur'),
	(2, '2', 'Yahya Ayyash', 'Tenaga Ahli', 'Jakarta Timur'),
	(3, '3', 'IRAWATI NASUTION', 'Manager Keuangan', 'Jakarta'),
	(4, '4', 'SIT MARKONAH', 'Manager Keuangan', 'sdafda'),
	(5, '5', 'Coba lagi', 'Tenaga Ahli', 'dsafdaf'),
	(6, '6', '455', 'Manager Keuangan', 'dsa'),
	(7, '7', 'Samsul', 'Manager Keuangan', 'Bor');

-- membuang struktur untuk table spkhop.hasil_alternatif
CREATE TABLE IF NOT EXISTS `hasil_alternatif` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_alternatif` int NOT NULL,
  `skor` double NOT NULL,
  `peringkat` int NOT NULL,
  `rekomendasi` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id_alternatif` (`id_alternatif`),
  CONSTRAINT `hasil_alternatif_ibfk_1` FOREIGN KEY (`id_alternatif`) REFERENCES `alternatif` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Membuang data untuk tabel spkhop.hasil_alternatif: ~7 rows (lebih kurang)
INSERT INTO `hasil_alternatif` (`id`, `id_alternatif`, `skor`, `peringkat`, `rekomendasi`) VALUES
	(23, 5, 0.17009026070356048, 1, 'Direkomendasikan'),
	(24, 2, 0.15735783115873717, 2, 'Direkomendasikan'),
	(25, 1, 0.14437960698229974, 3, 'Direkomendasikan'),
	(26, 3, 0.14121647224222228, 4, '-'),
	(27, 4, 0.13113152596561428, 5, '-'),
	(28, 7, 0, 6, '-'),
	(29, 6, 0, 7, '-');

-- membuang struktur untuk table spkhop.kriteria
CREATE TABLE IF NOT EXISTS `kriteria` (
  `id` int NOT NULL AUTO_INCREMENT,
  `kode` varchar(50) DEFAULT NULL,
  `nama` varchar(50) DEFAULT NULL,
  `keterangan` text,
  `bobot` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Membuang data untuk tabel spkhop.kriteria: ~4 rows (lebih kurang)
INSERT INTO `kriteria` (`id`, `kode`, `nama`, `keterangan`, `bobot`) VALUES
	(1, 'K1', 'Kompetensi', 'Tentang Kompetensi ', 0.5476810602456589),
	(2, 'K2', 'Disiplin', 'Tentang disiplin Karyawan sjasjasada', 0),
	(3, 'K3', 'Tanggung Jawab', 'Tanggung Jawb Karyawan', 0.14482838331509698),
	(4, 'K4', 'Kerja sama', 'Tentang Kerjasama Karyawan', 0.051666253491678055);

-- membuang struktur untuk table spkhop.matrix_alternatif
CREATE TABLE IF NOT EXISTS `matrix_alternatif` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_alternatif` int NOT NULL,
  `id_kriteria` int NOT NULL,
  `nilai` float NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_alternatif` (`id_alternatif`),
  KEY `id_kriteria` (`id_kriteria`),
  CONSTRAINT `matrix_alternatif_ibfk_1` FOREIGN KEY (`id_alternatif`) REFERENCES `alternatif` (`id`) ON DELETE CASCADE,
  CONSTRAINT `matrix_alternatif_ibfk_2` FOREIGN KEY (`id_kriteria`) REFERENCES `kriteria` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Membuang data untuk tabel spkhop.matrix_alternatif: ~17 rows (lebih kurang)
INSERT INTO `matrix_alternatif` (`id`, `id_alternatif`, `id_kriteria`, `nilai`) VALUES
	(1, 1, 1, 80),
	(2, 1, 2, 90),
	(3, 1, 3, 90),
	(4, 1, 4, 90),
	(5, 2, 1, 90),
	(6, 2, 2, 90),
	(7, 2, 3, 90),
	(8, 2, 4, 90),
	(9, 3, 1, 78),
	(10, 3, 2, 67),
	(11, 3, 3, 89),
	(12, 3, 4, 88),
	(13, 5, 1, 99),
	(14, 5, 2, 99),
	(15, 5, 3, 90),
	(16, 5, 4, 99),
	(17, 4, 1, 75),
	(18, 4, 2, 75),
	(19, 4, 3, 75),
	(20, 4, 4, 75);

-- membuang struktur untuk table spkhop.matrix_normalisasi
CREATE TABLE IF NOT EXISTS `matrix_normalisasi` (
  `id` int NOT NULL AUTO_INCREMENT,
  `kriteria_row` varchar(50) DEFAULT NULL,
  `kriteria_col` varchar(50) DEFAULT NULL,
  `nilai` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=129 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Membuang data untuk tabel spkhop.matrix_normalisasi: ~16 rows (lebih kurang)
INSERT INTO `matrix_normalisasi` (`id`, `kriteria_row`, `kriteria_col`, `nilai`) VALUES
	(113, 'Kompetensi', 'Kompetensi', 0.5965875193890944),
	(114, 'Kompetensi', 'Disiplin', 0.6666666666666666),
	(115, 'Kompetensi', 'Tanggung Jawab', 0.5454525619906836),
	(116, 'Kompetensi', 'Kerja Sama', 0.35),
	(117, 'Disiplin', 'Kompetensi', 0.19884262021238516),
	(118, 'Disiplin', 'Disiplin', 0.2222222222222222),
	(119, 'Disiplin', 'Tanggung Jawab', 0.3272715371944102),
	(120, 'Disiplin', 'Kerja Sama', 0.3),
	(121, 'Tanggung Jawab', 'Kompetensi', 0.11931750387781889),
	(122, 'Tanggung Jawab', 'Disiplin', 0.07406666666666667),
	(123, 'Tanggung Jawab', 'Tanggung Jawab', 0.10909051239813673),
	(124, 'Tanggung Jawab', 'Kerja Sama', 0.3),
	(125, 'Kerja Sama', 'Kompetensi', 0.0852523565207016),
	(126, 'Kerja Sama', 'Disiplin', 0.03704444444444444),
	(127, 'Kerja Sama', 'Tanggung Jawab', 0.01818538841676939),
	(128, 'Kerja Sama', 'Kerja Sama', 0.05);

-- membuang struktur untuk table spkhop.users
CREATE TABLE IF NOT EXISTS `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `role` enum('admin','manager','pegawai') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Membuang data untuk tabel spkhop.users: ~3 rows (lebih kurang)
INSERT INTO `users` (`id`, `username`, `password`, `role`) VALUES
	(1, 'admin', '1234', 'admin'),
	(2, 'direktur', '1234', 'manager'),
	(3, 'pegawai', '1234', 'pegawai'),
	(4, 'coba', '1234', 'pegawai');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
