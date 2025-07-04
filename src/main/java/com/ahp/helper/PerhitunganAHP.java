package com.ahp.helper;

import java.util.Arrays;

public class PerhitunganAHP {
    private final int n;
    private final double[][] matrix;
    private double[][] matrixNormalisasi;
    private double[] prioritas;
    private double CI;
    private double CR;

    private final double[] RI = {
        0.0, 0.0, 0.52, 0.89, 1.11,
        1.25, 1.35, 1.40, 1.45, 1.49
    };

    public PerhitunganAHP(double[][] matrix) {
        this.n = matrix.length;
        this.matrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            this.matrix[i] = Arrays.copyOf(matrix[i], n);
        }
        hitung();
    }

    private void hitung() {
        matrixNormalisasi = new double[n][n];
        double[] jumlahKolom = new double[n];

        // Hitung jumlah kolom
        for (int j = 0; j < n; j++) {
            double sum = 0;
            for (int i = 0; i < n; i++) {
                sum += matrix[i][j];
            }
            jumlahKolom[j] = sum;
        }

        // Normalisasi matriks
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrixNormalisasi[i][j] = matrix[i][j] / jumlahKolom[j];
            }
        }

        // Hitung prioritas (rata-rata baris normalisasi)
        prioritas = new double[n];
        for (int i = 0; i < n; i++) {
            double sumBaris = 0;
            for (int j = 0; j < n; j++) {
                sumBaris += matrixNormalisasi[i][j];
            }
            prioritas[i] = sumBaris / n;
        }

        // Hitung lambda max
        double lambdaMax = 0;
        for (int i = 0; i < n; i++) {
            double sumBaris = 0;
            for (int j = 0; j < n; j++) {
                sumBaris += matrix[i][j] * prioritas[j];
            }
            lambdaMax += sumBaris / prioritas[i];
        }
        lambdaMax /= n;

        // Hitung CI dan CR
        CI = (lambdaMax - n) / (n - 1);
        CR = RI.length > n ? CI / RI[n] : 0;
    }

    public double[] getPrioritas() {
        return prioritas;
    }

    public double[][] getMatrixNormalisasi() {
        return matrixNormalisasi;
    }

    public double getCI() {
        return CI;
    }

    public double getCR() {
        return CR;
    }
}
