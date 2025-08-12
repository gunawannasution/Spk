package com.ahp.util;

import com.ahp.content.pesanError;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/spkhop";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            pesanError.showCustomError(
                "Koneksi Gagal",
                "Tidak dapat terhubung ke database. Pastikan Database Di Nyalakan\nError: " + e.getMessage()
            );
            return null;
        }
    }
}
