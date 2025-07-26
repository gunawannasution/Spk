package com.ahp.content.dao;

import com.ahp.content.model.Kriteria;
import com.ahp.content.model.Matrix;
import com.ahp.util.DBConnection;
import static com.ahp.util.DBConnection.getConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MatrixDAOImpl implements MatrixDAO{    
    public MatrixDAOImpl(){}
   
    @Override
    public boolean simpanMatrix(List<Matrix> matrixData){
        String sqlInsert = "INSERT INTO matrix_normalisasi (kriteria_row, kriteria_col, nilai) VALUES (?, ?, ?)";
        String sqlDelete = "DELETE FROM matrix_normalisasi"; // Hapus data lama sebelum insert baru

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement deleteStmt = conn.prepareStatement(sqlDelete)) {
                deleteStmt.executeUpdate();
            }

            try (PreparedStatement insertStmt = conn.prepareStatement(sqlInsert)) {
                for (Matrix mn : matrixData) {
                    insertStmt.setString(1, mn.getKriteriaRow());
                    insertStmt.setString(2, mn.getKriteriaCol());
                    insertStmt.setDouble(3, mn.getNilai());
                    insertStmt.addBatch();
                }
                insertStmt.executeBatch();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public double[][] ambilMatrixNormalisasi(List<String> kriteriaList)
            throws Exception {int n = kriteriaList.size();
        double[][] matrix = new double[n][n];

        String sql = "SELECT kriteria_row, kriteria_col, nilai FROM matrix_normalisasi";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String row = rs.getString("kriteria_row");
                String col = rs.getString("kriteria_col");
                double nilai = rs.getDouble("nilai");

                int i = kriteriaList.indexOf(row);
                int j = kriteriaList.indexOf(col);

                if (i != -1 && j != -1) {
                    matrix[i][j] = nilai;
                }
            }
        }

        return matrix;
    }

    @Override
    public List<Matrix> getAll() {
        List<Matrix> list = new ArrayList<>();
        String sql = "SELECT * FROM matrix_normalisasi ORDER BY id";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Matrix k = new Matrix(
                        rs.getInt("id"),
                        rs.getString("kriteria_row"),
                        rs.getString("kriteria_col"),
                        rs.getDouble("nilai")
                );
                list.add(k);
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil kriteria: " + e.getMessage());
        }

        return list;
    }
}
