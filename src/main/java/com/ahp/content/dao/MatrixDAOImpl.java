package com.ahp.content.dao;

import com.ahp.content.model.Matrix;
import static com.ahp.util.DBConnection.getConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
}
