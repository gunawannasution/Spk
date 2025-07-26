package com.ahp.content.model;

import com.ahp.content.dao.PdfExportable;
import java.util.List;

public class Matrix implements PdfExportable{
    private int id; // auto-increment
    private String kriteriaRow;
    private String kriteriaCol;
    private double nilai;

    // Constructor utama
    public Matrix(int id, String kriteriaRow, String kriteriaCol, double nilai) {
        this.id = id;
        this.kriteriaRow = kriteriaRow;
        this.kriteriaCol = kriteriaCol;
        this.nilai = nilai;
    }

    // Constructor tambahan (untuk penggunaan di MatrixPanel)
    public Matrix(String kriteriaRow, String kriteriaCol, double nilai) {
        this(0, kriteriaRow, kriteriaCol, nilai);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKriteriaRow() {
        return kriteriaRow;
    }

    public void setKriteriaRow(String kriteriaRow) {
        this.kriteriaRow = kriteriaRow;
    }

    public String getKriteriaCol() {
        return kriteriaCol;
    }

    public void setKriteriaCol(String kriteriaCol) {
        this.kriteriaCol = kriteriaCol;
    }

    public double getNilai() {
        return nilai;
    }

    public void setNilai(double nilai) {
        this.nilai = nilai;
    }
    @Override
    public List<String> toPdfRow() {
        return List.of(
//            String.valueOf(id),
            kriteriaRow,
            kriteriaCol,
            String.format("%.4f", nilai) // ✅ Format 4 angka di belakang koma
        );
    }
}
