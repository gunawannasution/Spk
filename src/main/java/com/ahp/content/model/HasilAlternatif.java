package com.ahp.content.model;

import com.ahp.content.dao.PdfExportable;
import java.util.List;

public class HasilAlternatif implements PdfExportable {
    
    private int id;
    private int idAlternatif;
    private double skor;
    private int peringkat;
    private String rekomendasi;
    public HasilAlternatif(){}
    public HasilAlternatif(int id, int idAlternatif, double skor, int peringkat, String rekomendasi) {
        this.id = id;
        this.idAlternatif = idAlternatif;
        this.skor = skor;
        this.peringkat = peringkat;
        this.rekomendasi = rekomendasi;
    }

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

    
    
    @Override
    public List<String> toPdfRow() {
    return List.of(
        String.valueOf(idAlternatif),
        String.valueOf(peringkat),
        String.format("%.4f", skor),
        String.valueOf(rekomendasi)
    );
}

}
