package com.ahp.content.model;
import java.util.List;

public class Kriteria implements PdfExportable {
    private int id;
    private String kode;
    private String nama;
    private String ket;
    private double bobot;

    public Kriteria() {}

    
    public Kriteria(int id, String kode, String nama, String ket, double bobot) {
        this.id = id;
        this.kode = kode;
        this.nama = nama;
        this.ket = ket;
        this.bobot = bobot;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKet() {
        return ket;
    }

    public void setKet(String ket) {
        this.ket = ket;
    }

    public double getBobot() {
        return bobot;
    }

    public void setBobot(double bobot) {
        this.bobot = bobot;
    }

    @Override
    public String toString() {
        return nama + " (" + kode + ")";
    }
    
    @Override
    public List<String> toPdfRow() {
        return List.of(String.valueOf(id),kode,nama,ket,String.valueOf(bobot));
    }
}
