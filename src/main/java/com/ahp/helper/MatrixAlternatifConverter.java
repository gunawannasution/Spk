package com.ahp.helper;

import com.ahp.content.model.MatrixAlternatif;
import com.ahp.content.model.MatrixAlternatifPrint;
import java.util.*;

public class MatrixAlternatifConverter {

    public static List<MatrixAlternatifPrint> groupByAlternatif(
            List<MatrixAlternatif> dataMentah,
            int jumlahKriteria,
            Map<Integer, String> alternatifMap) {

        Map<Integer, MatrixAlternatifPrint> map = new LinkedHashMap<>();

        for (MatrixAlternatif ma : dataMentah) {
            int idAlt = ma.getIdAlternatif();
            map.putIfAbsent(idAlt, new MatrixAlternatifPrint(alternatifMap.get(idAlt), jumlahKriteria));
            MatrixAlternatifPrint print = map.get(idAlt);
            print.setNilai(ma.getIdKriteria() - 1, ma.getNilai());
        }

        int nomor = 1;
        for (MatrixAlternatifPrint print : map.values()) {
            print.setNo(nomor++);
        }

        return new ArrayList<>(map.values());
    }

    public static String[] buatHeaders(int jumlahKriteria, List<String> namaKriteriaList) {
        String[] headers = new String[jumlahKriteria + 2];
        headers[0] = "No";
        headers[1] = "Nama Karyawan";
        for (int i = 0; i < jumlahKriteria; i++) {
            headers[i + 2] = namaKriteriaList.get(i);
        }
        return headers;
    }
}
