
package com.ahp.login;

import com.ahp.util.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RegisterUser extends JDialog {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public RegisterUser(Window parent) {
        super(parent, "Create New Account", ModalityType.APPLICATION_MODAL);
        setSize(400, 250);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        add(passwordField, gbc);

        // Register Button
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton registerBtn = new JButton("Register");
        registerBtn.setBackground(new Color(0, 123, 255));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFocusPainted(false);
        registerBtn.setPreferredSize(new Dimension(120, 35));
        registerBtn.addActionListener(e -> register());
        add(registerBtn, gbc);
    }

    private void register() {
        String username = usernameField.getText().trim();
        String password = String.valueOf(passwordField.getPassword()).trim();
        String role = "pegawai"; // Otomatis role 'karyawan'

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field wajib diisi!");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            // Cek apakah username sudah ada
            String checkSql = "SELECT * FROM users WHERE username = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, username);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(this, "Username sudah digunakan!", "Gagal", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
            }

            // Simpan user baru
            String insertSql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                stmt.setString(1, username);
                stmt.setString(2, password); // Untuk keamanan, sebaiknya di-hash
                stmt.setString(3, role);

                int result = stmt.executeUpdate();
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Registrasi berhasil! Silakan login.");
                    dispose(); // tutup dialog
                } else {
                    JOptionPane.showMessageDialog(this, "Registrasi gagal. Coba lagi.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menyimpan ke database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
