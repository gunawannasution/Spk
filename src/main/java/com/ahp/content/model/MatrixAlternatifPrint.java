package com.ahp.content.model;

import com.ahp.content.dao.PdfExportable;
import java.util.ArrayList;
import java.util.List;

public class MatrixAlternatifPrint implements PdfExportable {
    private int no; // nomor urut
    private final String namaAlternatif;
    private final List<Double> nilaiKriteria;

    public MatrixAlternatifPrint(String namaAlternatif, int jumlahKriteria) {
        this.namaAlternatif = namaAlternatif;
        this.nilaiKriteria = new ArrayList<>();
        for (int i = 0; i < jumlahKriteria; i++) {
            nilaiKriteria.add(0.0);
        }
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public void setNilai(int index, double nilai) {
        if (index >= 0 && index < nilaiKriteria.size()) {
            nilaiKriteria.set(index, nilai);
        }
    }

    public String getNamaAlternatif() {
        return namaAlternatif;
    }

    public List<Double> getNilaiKriteria() {
        return nilaiKriteria;
    }

    @Override
    public List<String> toPdfRow() {
        List<String> row = new ArrayList<>();
        row.add(String.valueOf(no));
        row.add(namaAlternatif);
        for (Double nilai : nilaiKriteria) {
            row.add(String.format("%.4f", nilai));
        }
        return row;
    }
}
