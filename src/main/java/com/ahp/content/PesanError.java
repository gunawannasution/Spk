package com.ahp.content;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.*;

public class pesanError {

    // Menampilkan informasi biasa
    public static void showInfo(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Informasi", JOptionPane.INFORMATION_MESSAGE);
    }

    // Menampilkan pesan error sederhana
    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Terjadi Kesalahan", JOptionPane.ERROR_MESSAGE);
    }

    // Menampilkan dialog konfirmasi (YES/NO)
    public static boolean confirm(Component parent, String message) {
        int result = JOptionPane.showConfirmDialog(parent, message, "Konfirmasi", JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }

    // Menampilkan pesan error kustom (merah)
    public static void showCustomError(String title, String message) {
        CustomErrorDialog dialog = new CustomErrorDialog(title, message);
        dialog.setVisible(true);
    }

    private static class CustomErrorDialog extends JDialog {

        public CustomErrorDialog(String title, String message) {
            setTitle(title);
            setModal(true);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setSize(400, 200);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout());

            // Header merah
            JPanel topPanel = new JPanel();
            topPanel.setBackground(new Color(220, 53, 69));
            JLabel titleLabel = new JLabel(title);
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            topPanel.add(titleLabel);
            add(topPanel, BorderLayout.NORTH);

            // Pesan
            JPanel centerPanel = new JPanel(new BorderLayout());
            JLabel messageLabel = new JLabel("<html><div style='text-align: center;'>" + message + "</div></html>");
            messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            centerPanel.add(messageLabel, BorderLayout.CENTER);
            add(centerPanel, BorderLayout.CENTER);

            // Tombol OK
            JPanel bottomPanel = new JPanel();
            JButton okButton = new JButton("OK");
            okButton.setBackground(new Color(220, 53, 69));
            okButton.setForeground(Color.WHITE);
            okButton.setFocusPainted(false);
            okButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
            okButton.setPreferredSize(new Dimension(80, 30));
            okButton.addActionListener(e -> dispose());
            bottomPanel.add(okButton);
            add(bottomPanel, BorderLayout.SOUTH);
        }
    }
}

