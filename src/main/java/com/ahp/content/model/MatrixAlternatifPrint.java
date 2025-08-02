package com.ahp.content.model;

import com.ahp.content.dao.PdfExportable;
import java.util.ArrayList;
import java.util.List;

public class MatrixAlternatifPrint implements PdfExportable {
    private final int no;
    private final String namaAlternatif;
    private final List<Double> nilaiKriteria;

    public MatrixAlternatifPrint(int no, String namaAlternatif, List<Double> nilaiKriteria) {
        this.no = no;
        this.namaAlternatif = namaAlternatif;
        this.nilaiKriteria = nilaiKriteria;
    }
    

    @Override
    public List<String> toPdfRow() {
        List<String> row = new ArrayList<>();
        row.add(namaAlternatif);
        for (Double nilai : nilaiKriteria) {
            row.add(String.valueOf(Math.round(nilai)));
            //row.add(String.format("%.4f", nilai));
        }
        return row;
    }
}
