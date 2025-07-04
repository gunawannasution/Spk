package com.ahp.content.model;

public class Karyawan {
    int id;
    String nik,nama,jabatan,alamat;

    public Karyawan() {
    }
    
    public Karyawan(int id, String nik, String nama, String jabatan, String alamat) {
        this.id = id;
        this.nik = nik;
        this.nama = nama;
        this.jabatan = jabatan;
        this.alamat = alamat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
     @Override
    public String toString() {
        return nama; // Menampilkan nama karyawan di combobox
    }
    
}
