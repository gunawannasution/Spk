package com.ahp.helper;

import com.ahp.content.model.MatrixAlternatif;
import com.ahp.content.model.MatrixAlternatifPrint;

import java.util.*;

public class MatrixAlternatifConverter {

    /**
     * Group dan konversi data mentah MatrixAlternatif ke list yang siap print PDF.
     * @param dataMentah list data MatrixAlternatif mentah dari database
     * @param jumlahKriteria total kriteria (misal 4)
     * @param alternatifMap map dari idAlternatif ke namaAlternatif
     * @return list MatrixAlternatifPrint yang sudah lengkap dengan nomor urut dan nilai per kriteria
     */
    public static List<MatrixAlternatifPrint> groupByAlternatif(
            List<MatrixAlternatif> dataMentah,
            int jumlahKriteria,
            Map<Integer, String> alternatifMap) {

        // Map dari idAlternatif ke MatrixAlternatifPrint (hasil akhir)
        Map<Integer, MatrixAlternatifPrint> map = new LinkedHashMap<>();

        // Proses setiap record mentah
        for (MatrixAlternatif ma : dataMentah) {
            int idAlt = ma.getIdAlternatif();
            // Jika belum ada, buat objek baru dengan nama alternatif dari map dan nilai 0 untuk setiap kriteria
            map.putIfAbsent(idAlt, new MatrixAlternatifPrint(alternatifMap.get(idAlt), jumlahKriteria));

            // Ambil objek print yang sudah dibuat
            MatrixAlternatifPrint print = map.get(idAlt);

            // Set nilai untuk kriteria tertentu (index dimulai 0)
            int kIndex = ma.getIdKriteria() - 1;
            print.setNilai(kIndex, ma.getNilai());
        }

        // Beri nomor urut mulai dari 1 untuk setiap alternatif
        int nomor = 1;
        for (MatrixAlternatifPrint print : map.values()) {
            print.setNo(nomor++);
        }

        // Kembalikan hasil sebagai list
        return new ArrayList<>(map.values());
    }

    /**
     * Buat header kolom sesuai kebutuhan seperti gambar contoh:
     * No, Alternatif, Kompetensi, Disiplin, Tanggung Jawab, Kerjasama
     * @param jumlahKriteria jumlah kriteria
     * @param namaKriteriaList list nama kriteria berurutan (Kompetensi, Disiplin, dll)
     * @return array header lengkap
     */
    public static String[] buatHeaders(int jumlahKriteria, List<String> namaKriteriaList) {
        String[] headers = new String[jumlahKriteria + 2];
        headers[0] = "No";
        headers[1] = "Alternatif";
        for (int i = 0; i < jumlahKriteria; i++) {
            headers[i + 2] = namaKriteriaList.get(i);
        }
        return headers;
    }
}
