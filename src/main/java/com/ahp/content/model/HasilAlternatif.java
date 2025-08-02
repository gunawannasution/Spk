package com.ahp.content.model;

import com.ahp.content.dao.PdfExportable;
import java.util.List;

public class HasilAlternatif implements PdfExportable {
    
    private int id;
    private int idAlternatif;
    private double skor;
    private int peringkat;
    private String rekomendasi;

    public HasilAlternatif(int id, int idAlternatif, double skor, int peringkat, String rekomendasi) {
        this.id = id;
        this.idAlternatif = idAlternatif;
        this.skor = skor;
        this.peringkat = peringkat;
        this.rekomendasi = rekomendasi;
    }

    public int getId() { return id; }
    public int getIdAlternatif() { return idAlternatif; }
    public double getSkor() { return skor; }
    public int getPeringkat() { return peringkat; }
    public String getRekomendasi() { return rekomendasi; }
    
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
