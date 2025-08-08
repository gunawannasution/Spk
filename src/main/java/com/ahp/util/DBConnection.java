package com.ahp.util;
import com.ahp.content.pesanError;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection conn;
    public static Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                String url = "jdbc:mysql://localhost:3306/spkhop";
                String user = "root";
                String password = ""; 
                conn = DriverManager.getConnection(url, user, password);
            }
        } catch (SQLException e) {
            pesanError.showCustomError("Koneksi Gagal", "Tidak dapat terhubung ke database. Pastikan Database Di Nyalakan\nError: " + e.getMessage());
        }
        return conn;
    }
}


