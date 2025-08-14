package com.ahp.content.model;

import com.ahp.content.dao.PdfExportable;
import java.util.List;

/**
 * Model untuk menyimpan hasil alternatif termasuk informasi dari tabel alternatif.
 */
public class HasilAlternatif implements PdfExportable {

    private int id;
    private int idAlternatif;
    private double skor;
    private int peringkat;
    private String rekomendasi;
    private String namaAlternatif;
    // Data mapping dari tabel alternatif
    private String nik;
    private String nama;
    private String jabatan;
    private String alamat;

    // Constructor kosong
    public HasilAlternatif() {}

    // Constructor utama
    public HasilAlternatif(int id, int idAlternatif, double skor, int peringkat, String rekomendasi) {
        this.id = id;
        this.idAlternatif = idAlternatif;
        this.skor = skor;
        this.peringkat = peringkat;
        this.rekomendasi = rekomendasi;
    }

    // Getter dan Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdAlternatif() {
        return idAlternatif;
    }

    public void setIdAlternatif(int idAlternatif) {
        this.idAlternatif = idAlternatif;
    }
    
    public String getNamaAlternatif() {
        return namaAlternatif;
    }

    public void setNamaAlternatif(String namaAlternatif) {
        this.namaAlternatif = namaAlternatif;
    }

    public double getSkor() {
        return skor;
    }

    public void setSkor(double skor) {
        this.skor = skor;
    }

    public int getPeringkat() {
        return peringkat;
    }

    public void setPeringkat(int peringkat) {
        this.peringkat = peringkat;
    }

    public String getRekomendasi() {
        return rekomendasi;
    }

    public void setRekomendasi(String rekomendasi) {
        this.rekomendasi = rekomendasi;
    }

    // Getter & Setter untuk data dari tabel alternatif
    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    /**
     * Digunakan untuk ekspor data ke PDF.
     */
    @Override
    public List<String> toPdfRow() {
        return List.of(
            String.valueOf(peringkat),
            namaAlternatif != null ? namaAlternatif : "Tidak ditemukan",
            String.format("%.4f", skor),
            rekomendasi != null ? rekomendasi : "-"
        );
    }
}
