package com.ahp.helper;
import com.ahp.util.DBConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LogService {
    public static void insertLog(String userId, String aktivitas, String detail) {
        String sql = "INSERT INTO log_aktivitas (user_id, aktivitas, detail) VALUES (?, ?, ?)";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, userId);
            stmt.setString(2, aktivitas);
            stmt.setString(3, detail);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Gagal mencatat log aktivitas: " + e.getMessage());
        }
    }
}
